package dev.erhahahaa.storyapp.utils.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody

fun File.asRequestBody(contentType: String = "image/jpeg"): RequestBody {
  return this.asRequestBody(contentType.toMediaType())
}

fun File.compressImage(targetSize: Int = 1_000_000): File {
  val originalSizeInPercentage = length().toDouble() / targetSize
  val quality =
    when {
      originalSizeInPercentage < 1.5 -> 100
      else -> (100 / originalSizeInPercentage).toInt()
    }
  val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
  BitmapFactory.decodeFile(absolutePath, options)

  val scaledBitmap = BitmapFactory.decodeFile(absolutePath)
  val compressedFile =
    File.createTempFile("JPEG_${System.currentTimeMillis()}_", ".jpg").apply {
      FileOutputStream(this).use { out ->
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
      }
    }

  return if (compressedFile.length() > targetSize) compressedFile.compressImage(targetSize)
  else compressedFile
}
