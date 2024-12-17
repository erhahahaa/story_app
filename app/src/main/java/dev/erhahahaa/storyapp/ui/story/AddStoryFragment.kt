package dev.erhahahaa.storyapp.ui.story

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.location.*
import dev.erhahahaa.storyapp.R
import dev.erhahahaa.storyapp.data.model.User
import dev.erhahahaa.storyapp.databinding.FragmentAddStoryBinding
import dev.erhahahaa.storyapp.utils.extensions.compressImage
import dev.erhahahaa.storyapp.utils.extensions.getViewModelFactory
import dev.erhahahaa.storyapp.utils.extensions.hideKeyboard
import dev.erhahahaa.storyapp.viewmodel.MainViewModel
import dev.erhahahaa.storyapp.viewmodel.StoryViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AddStoryFragment : Fragment() {
  private var _binding: FragmentAddStoryBinding? = null
  private val binding
    get() = _binding!!

  private lateinit var fusedLocationClient: FusedLocationProviderClient
  private var locationCallback: LocationCallback? = null
  private var location: Location? = null

  private val mainViewModel: MainViewModel by activityViewModels {
    requireContext().getViewModelFactory()
  }
  private val storyViewModel: StoryViewModel by activityViewModels {
    requireContext().getViewModelFactory()
  }

  private var user: User? = null
  private var imageFile: File? = null
  private var imageUri: Uri? = null

  private val permissionsLauncher =
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
      when {
        permissions[Manifest.permission.CAMERA] == true -> launchCamera()
        permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> initializeLocation()
      }
    }

  private val getImageFromGallery =
    registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
      uri?.let { handleImageSelection(it) }
    }

  private val getImageFromCamera =
    registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
      if (isSuccess && imageUri != null) {
        handleImageSelection(imageUri!!)
      } else {
        showError(getString(R.string.error_capturing_image))
      }
    }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    setupObservers()
    setupListeners()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
    imageFile?.delete()
    _binding = null
  }

  private fun handleImageSelection(uri: Uri) {
    try {
      createImageFile().apply {
        copyUriToFile(uri, this)
        imageFile = if (length() > 1_000_000) compressImage() else this
        imageUri = getUriForFile(this)
        binding.ivImage.setImageURI(imageUri)
        updateFormState()
      }
    } catch (e: IOException) {
      showError(getString(R.string.error_processing_image))
    }
  }

  private fun setupObservers() {
    var isLoading = false

    with(viewLifecycleOwner) {
      mainViewModel.user.observe(this) { user = it }

      storyViewModel.storyFormState.observe(this) { state ->
        binding.apply {
          buttonAdd.isEnabled = state?.isDataValid == true
          edAddDescription.error = state?.descriptionError?.let { getString(it) }
        }
      }

      storyViewModel.isLoading.observe(this) { value ->
        binding.buttonAdd.apply {
          isEnabled = !value
          setLoading(value)
        }
        isLoading = value
      }

      storyViewModel.addStoryResult.observe(this) { result ->
        result?.let {
          showMessage(it.message)
          if (!it.error && !isLoading) {
            requireActivity().onBackPressedDispatcher.onBackPressed()
          }
        }
      }
    }
  }

  private fun setupListeners() {
    binding.apply {
      edAddDescription.addTextChangedListener(
        object : TextWatcher {
          override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

          override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
            updateFormState()

          override fun afterTextChanged(s: Editable?) {}
        }
      )

      btnCamera.setOnClickListener {
        when {
          hasPermission(Manifest.permission.CAMERA) -> launchCamera()
          shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ->
            showMessage(getString(R.string.camera_permission_required))
          else -> permissionsLauncher.launch(arrayOf(Manifest.permission.CAMERA))
        }
      }
      btnGallery.setOnClickListener { launchGallery() }
      buttonAdd.setOnClickListener { submitStory() }
      switchLocation.setOnCheckedChangeListener { _, isChecked ->
        if (isChecked) {
          checkLocationPermissions()
        } else {
          locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
          location = null
          updateFormState()
        }
      }
    }
  }

  private fun initializeLocation() {
    if (!hasLocationPermissions()) return

    try {
      locationCallback =
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
              location = result.lastLocation
              updateFormState()
            }
          }
          .also { callback ->
            val locationRequest =
              LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10_000L)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(5_000L)
                .setMaxUpdateDelayMillis(30_000L)
                .build()

            fusedLocationClient.requestLocationUpdates(
              locationRequest,
              callback,
              Looper.getMainLooper(),
            )

            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
              location = loc
              updateFormState()
            }
          }
    } catch (e: SecurityException) {
      showError(getString(R.string.error_getting_location))
    }
  }

  private fun checkLocationPermissions() {
    val permissions =
      arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    if (permissions.all { hasPermission(it) }) {
      initializeLocation()
    } else {
      permissionsLauncher.launch(permissions)
    }
  }

  private fun hasLocationPermissions() =
    hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
      hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)

  private fun hasPermission(permission: String): Boolean =
    ActivityCompat.checkSelfPermission(requireContext(), permission) ==
      PackageManager.PERMISSION_GRANTED

  private fun submitStory() {
    val description = binding.edAddDescription.text.toString()
    val token =
      user?.token
        ?: run {
          showError(getString(R.string.error_user_not_logged_in))
          return
        }

    imageFile?.let {
      showMessage(getString(R.string.please_wait))
      activity?.hideKeyboard()

      if (binding.switchGuest.isChecked) {
        storyViewModel.addStoryGuest(it, description, location?.latitude, location?.longitude)
      } else {
        storyViewModel.addStory(token, it, description, location?.latitude, location?.longitude)
      }
    } ?: showError(getString(R.string.error_image_required))
  }

  private fun launchCamera() {
    try {
      createImageFile().also { file ->
        imageFile = file
        imageUri = getUriForFile(file)
        getImageFromCamera.launch(imageUri)
      }
    } catch (e: IOException) {
      showError(getString(R.string.error_creating_image_file))
    }
  }

  private fun launchGallery() {
    getImageFromGallery.launch(
      PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
    )
  }

  private fun updateFormState() {
    storyViewModel.storyDataChanged(binding.edAddDescription.text.toString(), imageFile, location)
  }

  @Throws(IOException::class)
  private fun createImageFile(): File =
    File.createTempFile("JPEG_${System.currentTimeMillis()}_", ".jpg", requireContext().cacheDir)
      .apply { deleteOnExit() }

  private fun getUriForFile(file: File): Uri =
    FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", file)

  private fun copyUriToFile(uri: Uri, file: File) {
    requireContext().contentResolver.openInputStream(uri)?.use { input ->
      FileOutputStream(file).use { output -> input.copyTo(output) }
    }
  }

  private fun showError(message: String) = showMessage(message)

  private fun showMessage(message: String) {
    if (isAdded) {
      Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
  }
}
