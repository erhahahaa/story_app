package dev.erhahahaa.storyapp.utils.extensions

import android.content.Context
import android.location.Geocoder
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import dev.erhahahaa.storyapp.data.api.ApiConfig
import dev.erhahahaa.storyapp.data.prefs.UserPreferences
import dev.erhahahaa.storyapp.data.repository.StoryRepository
import dev.erhahahaa.storyapp.data.repository.UserRepository
import dev.erhahahaa.storyapp.di.ViewModelFactory
import java.util.Locale

fun Context.getViewModelFactory(): ViewModelFactory {
  val userPref = UserPreferences.getInstance(this)
  val apiService = ApiConfig.getApiService()
  val userRepo = UserRepository.getInstance(apiService, userPref)
  val storyRepo = StoryRepository.getInstance(apiService)
  return ViewModelFactory(userRepo, storyRepo)
}

fun Context.createLoader(): CircularProgressDrawable {
  return CircularProgressDrawable(this).apply {
    strokeWidth = 5f
    centerRadius = 30f
    start()
  }
}

fun Context.getAddressFromLocation(latitude: Double, longitude: Double): String {
  val geocoder = Geocoder(this, Locale.getDefault())
  val addresses = geocoder.getFromLocation(latitude, longitude, 1)

  return if (addresses != null) {
    if (addresses.isNotEmpty()) {
      val address = addresses[0]
      "${address.thoroughfare}, ${address.locality}, ${address.countryName}"
    } else {
      "Address not found"
    }
  } else {
    "Address not found"
  }
}
