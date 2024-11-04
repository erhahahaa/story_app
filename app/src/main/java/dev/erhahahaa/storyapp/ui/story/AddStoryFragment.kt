package dev.erhahahaa.storyapp.ui.story

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
  private lateinit var locationRequest: LocationRequest
  private lateinit var locationCallback: LocationCallback
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
      permissions[Manifest.permission.CAMERA]?.let { if (it) launchCamera() }
      permissions[Manifest.permission.ACCESS_FINE_LOCATION]?.let { if (it) getLocation() }
    }

  private val getImageFromGallery =
    registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
      uri?.let { handleImageSelection(it) }
    }

  private val getImageFromCamera =
    registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
      if (isSuccess && imageUri != null) handleImageSelection(imageUri!!)
      else showError(getString(R.string.error_capturing_image))
    }

  private fun handleImageSelection(uri: Uri) {
    try {
      val file =
        createImageFile().apply {
          copyUriToFile(uri, this)
          imageFile = if (length() > 1_000_000) compressImage() else this
        }
      imageUri = getUriForFile(file)
      binding.ivImage.setImageURI(imageUri)
      updateFormState()
    } catch (e: IOException) {
      showError(getString(R.string.error_processing_image))
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
    setupLocation()
    setupObservers()
    setupListeners()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
    fusedLocationClient.removeLocationUpdates(locationCallback)
    imageFile?.delete()
  }

  private fun setupLocation() {
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    locationRequest =
      LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10_000L)
        .setWaitForAccurateLocation(false)
        .setMinUpdateIntervalMillis(5_000L)
        .setMaxUpdateDelayMillis(30_000L)
        .build()

    locationCallback =
      object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
          super.onLocationResult(result)
          location = result.lastLocation
          updateFormState()
        }
      }
    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    checkLocationPermission()
  }

  private fun setupObservers() {
    var isLoading = false
    mainViewModel.user.observe(viewLifecycleOwner) { user -> this.user = user }

    storyViewModel.storyFormState.observe(viewLifecycleOwner) { state ->
      binding.buttonAdd.isEnabled = state?.isDataValid == true
      binding.edAddDescription.error = state?.descriptionError?.let { getString(it) }
    }

    storyViewModel.isLoading.observe(viewLifecycleOwner) { value ->
      binding.buttonAdd.isEnabled = !value
      binding.buttonAdd.setLoading(value)
      isLoading = value
    }

    storyViewModel.addStoryResult.observe(viewLifecycleOwner) { result ->
      if (result != null) {
        showMessage(result.message)
        if (!result.error && !isLoading) {
          val onBackPressedDispatcher = requireActivity().onBackPressedDispatcher
          onBackPressedDispatcher.onBackPressed()
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

      btnCamera.setOnClickListener { checkCameraPermission() }
      btnGallery.setOnClickListener { launchGallery() }
      buttonAdd.setOnClickListener { submitStory() }
    }
  }

  private fun updateFormState() {
    storyViewModel.storyDataChanged(binding.edAddDescription.text.toString(), imageFile, location)
  }

  private fun checkCameraPermission() {
    if (hasPermission(Manifest.permission.CAMERA)) {
      launchCamera()
    } else {
      handlePermissionRequest(Manifest.permission.CAMERA, R.string.camera_permission_required)
    }
  }

  private fun checkLocationPermission() {
    if (
      hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
        hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    ) {
      getLocation()
    } else {
      handlePermissionRequest(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        R.string.location_permission_required,
      )
      handlePermissionRequest(
        Manifest.permission.ACCESS_FINE_LOCATION,
        R.string.location_permission_required,
      )
    }
  }

  private fun handlePermissionRequest(permission: String, messageId: Int) {
    if (shouldShowRequestPermissionRationale(permission)) {
      showMessage(getString(messageId))
    } else {
      permissionsLauncher.launch(arrayOf(permission))
    }
  }

  private fun hasPermission(permission: String): Boolean =
    ActivityCompat.checkSelfPermission(requireContext(), permission) ==
      PackageManager.PERMISSION_GRANTED

  private fun launchCamera() {
    try {
      imageFile = createImageFile()
      imageUri = getUriForFile(imageFile!!)
      getImageFromCamera.launch(imageUri)
    } catch (e: IOException) {
      showError(getString(R.string.error_creating_image_file))
    }
  }

  private fun launchGallery() {
    getImageFromGallery.launch(
      PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
    )
  }

  private fun getLocation() {
    if (
      hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
        hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    ) {
      try {
        fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
          location = loc
          updateFormState()
          Log.d("AddStoryFragment", "Location: $loc")
        }
      } catch (e: SecurityException) {
        showError(getString(R.string.error_getting_location))
      }
    }
  }

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
      val isGuest = binding.switchGuest.isChecked
      if (isGuest) {
        storyViewModel.addStoryGuest(it, description, location?.latitude, location?.longitude)
      } else {
        storyViewModel.addStory(token, it, description, location?.latitude, location?.longitude)
      }
    } ?: showError(getString(R.string.error_image_required))
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

  private fun showError(message: String) {
    showMessage(message)
  }

  private fun showMessage(message: String) {
    if (isAdded) {
      Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
  }
}
