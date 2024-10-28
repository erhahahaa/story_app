package dev.erhahahaa.storyapp.utils.extensions

import android.util.Patterns
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

fun String.toRequestBody(contentType: String = "text/plain"): RequestBody {
  return this.toRequestBody(contentType.toMediaType())
}

fun String.isValidName(): Boolean {
  return isNotBlank() && length >= 3
}

fun String.isValidEmail(): Boolean {
  return isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
  return length >= 8
}
