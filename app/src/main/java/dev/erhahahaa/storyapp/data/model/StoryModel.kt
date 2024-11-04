package dev.erhahahaa.storyapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class StoryModel(
  val id: String,
  val name: String,
  val description: String,
  val photoUrl: String,
  val lat: Double?,
  val lon: Double?,
  val createdAt: String,
) : Parcelable
