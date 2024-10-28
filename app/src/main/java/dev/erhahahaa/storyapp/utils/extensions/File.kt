package dev.erhahahaa.storyapp.utils.extensions

import java.io.File
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody

fun File.asRequestBody(contentType: String = "image/jpeg"): RequestBody {
  return this.asRequestBody(contentType.toMediaType())
}
