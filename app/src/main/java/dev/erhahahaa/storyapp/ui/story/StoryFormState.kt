package dev.erhahahaa.storyapp.ui.story

data class StoryFormState(
  val descriptionError: Int? = null,
  val photoError: Int? = null,
  val locationError: Int? = null,
  val isDataValid: Boolean = false,
)
