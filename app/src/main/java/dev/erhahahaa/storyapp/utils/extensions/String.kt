package dev.erhahahaa.storyapp.utils.extensions

import java.io.File
import okhttp3.MediaType
import okhttp3.RequestBody

fun String.toMediaType(): MediaType {
  return MediaType.get(this)
}

fun String.toRequestBody(contentType: String = "text/plain"): RequestBody {
  return RequestBody.create(contentType.toMediaType(), this)
}

fun File.asRequestBody(contentType: String = "image/jpeg"): RequestBody {
  return RequestBody.create(contentType.toMediaType(), this)
}
