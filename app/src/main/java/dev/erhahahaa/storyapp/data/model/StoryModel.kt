package dev.erhahahaa.storyapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class StoryModel(
  val id: String,
  val name: String,
  val description: String,
  val photoUrl: String,
  val lat: Double?,
  val lon: Double?,
  val createdAt: String,
)
