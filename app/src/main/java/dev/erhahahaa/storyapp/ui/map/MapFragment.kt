package dev.erhahahaa.storyapp.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dev.erhahahaa.storyapp.R
import dev.erhahahaa.storyapp.data.model.StoryModel
import dev.erhahahaa.storyapp.data.model.User
import dev.erhahahaa.storyapp.databinding.FragmentMapBinding
import dev.erhahahaa.storyapp.utils.extensions.getViewModelFactory
import dev.erhahahaa.storyapp.utils.extensions.load
import dev.erhahahaa.storyapp.viewmodel.MainViewModel
import dev.erhahahaa.storyapp.viewmodel.StoryViewModel

class MapFragment : Fragment(), OnMapReadyCallback {
  private var _binding: FragmentMapBinding? = null
  private val binding
    get() = _binding!!

  private lateinit var googleMap: GoogleMap
  private val mainViewModel: MainViewModel by activityViewModels {
    requireContext().getViewModelFactory()
  }
  private val storyViewModel: StoryViewModel by activityViewModels {
    requireContext().getViewModelFactory()
  }
  private var user: User? = null
  private val storyMap = mutableMapOf<Marker, StoryModel>()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentMapBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    mapFragment.getMapAsync(this)
    setupObservers()
  }

  private fun setupObservers() {
    mainViewModel.user.observe(viewLifecycleOwner) { user ->
      this.user = user
      getStoriesWithLocation()
    }
    storyViewModel.storiesWithLocation.observe(viewLifecycleOwner) { storiesWithLocation ->
      if (::googleMap.isInitialized) {
        storiesWithLocation?.data?.forEach { story ->
          story.lat?.let { lat ->
            story.lon?.let { lon ->
              val position = LatLng(lat, lon)
              val marker = googleMap.addMarker(MarkerOptions().position(position).title(story.name))
              if (marker != null) {
                storyMap[marker] = story
              }
            }
          }
        }
      }
    }
  }

  private fun getStoriesWithLocation() {
    user?.token?.let { storyViewModel.getStoriesWithLocation(it) }
  }

  override fun onMapReady(map: GoogleMap) {
    googleMap = map
    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-6.200000, 106.816666), 5f))
    googleMap.setInfoWindowAdapter(CustomInfoWindowAdapter())
  }

  private inner class CustomInfoWindowAdapter : GoogleMap.InfoWindowAdapter {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getInfoWindow(marker: Marker): View? = null

    override fun getInfoContents(marker: Marker): View {
      val story = storyMap[marker]
      val view = inflater.inflate(R.layout.custom_info_window, null)

      val title: TextView = view.findViewById(R.id.info_title)
      val description: TextView = view.findViewById(R.id.info_description)
      val photo: ImageView = view.findViewById(R.id.info_image)

      story?.let {
        title.text = story.name
        description.text = story.description
        photo.load(story.photoUrl) {
          if (marker.isInfoWindowShown) {
            marker.hideInfoWindow()
            marker.showInfoWindow()
          }
        }
      }

      return view
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
