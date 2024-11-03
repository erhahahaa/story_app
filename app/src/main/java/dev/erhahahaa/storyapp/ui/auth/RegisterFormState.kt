package dev.erhahahaa.storyapp.ui.auth

data class RegisterFormState(
  val nameError: Int? = null,
  val emailError: Int? = null,
  val passwordError: Int? = null,
  val isDataValid: Boolean = false,
)
