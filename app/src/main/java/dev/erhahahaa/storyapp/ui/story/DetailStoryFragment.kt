package dev.erhahahaa.storyapp.ui.story

import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.erhahahaa.storyapp.R
import dev.erhahahaa.storyapp.data.model.StoryModel
import dev.erhahahaa.storyapp.databinding.FragmentDetailStoryBinding
import dev.erhahahaa.storyapp.utils.extensions.loadImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val STORY_DETAIL = "story_detail"

class DetailStoryFragment : Fragment() {

  private var _binding: FragmentDetailStoryBinding? = null
  private val binding
    get() = _binding!!

  private var story: StoryModel? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    @Suppress("DEPRECATION") arguments?.getParcelable<StoryModel>(STORY_DETAIL)?.let { story = it }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentDetailStoryBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    story?.let { displayStoryDetails(it) }
  }

  private fun displayStoryDetails(story: StoryModel) {
    binding.root.context.loadImage(story.photoUrl, binding.ivDetailPhoto)

    binding.apply {
      tvDetailName.text = story.name
      tvDetailDescription.text = story.description
      tvCreatedDate.text = getString(R.string.postedOn, formatDate(story.createdAt))
    }
    if (story.lat != null && story.lon != null) getLocation(story.lat, story.lon)
  }

  private fun getLocation(lat: Double, lon: Double) {
    binding.root.context.getAddress(lat, lon) { address ->
      if (address != null) {
        binding.llLocation.visibility = View.VISIBLE
        binding.tvLocation.text =
          getString(R.string.rawLocation, "${address.locality}, ${address.countryName}")
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private fun formatDate(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    val parsedDate = inputFormat.parse(date) ?: Date()
    return outputFormat.format(parsedDate)
  }
}

@Suppress("DEPRECATION")
fun Context.getAddress(
  latitude: Double,
  longitude: Double,
  address: (android.location.Address?) -> Unit,
) {
  val geocoder = Geocoder(this, Locale.getDefault())

  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    geocoder.getFromLocation(latitude, longitude, 1) { address(it.firstOrNull()) }
    return
  }

  try {
    address(geocoder.getFromLocation(latitude, longitude, 1)?.firstOrNull())
  } catch (e: Exception) {
    address(null)
  }
}
