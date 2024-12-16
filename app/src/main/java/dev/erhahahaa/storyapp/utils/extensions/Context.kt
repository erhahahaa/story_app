package dev.erhahahaa.storyapp.utils.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.CustomTarget
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

fun Context.loadImage(url: String, imageView: ImageView) {
  val circularDrawable = createLoader()
  Glide.with(this)
    .load(url)
    .transform(CenterCrop(), RoundedCorners(12))
    .placeholder(circularDrawable)
    .error(android.R.drawable.stat_notify_error)
    .into(imageView)
}

fun ImageView.load(url: String, onLoadComplete: (() -> Unit)? = null) {
  Glide.with(this.context)
    .load(url)
    .into(
      object : CustomTarget<Drawable>() {
        override fun onResourceReady(
          resource: Drawable,
          transition: com.bumptech.glide.request.transition.Transition<in Drawable>?,
        ) {
          this@load.setImageDrawable(resource)
          onLoadComplete?.invoke()
        }

        override fun onLoadCleared(placeholder: Drawable?) {
          this@load.setImageDrawable(placeholder)
        }
      }
    )
}
