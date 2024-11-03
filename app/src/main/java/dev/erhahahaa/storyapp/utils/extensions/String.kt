package dev.erhahahaa.storyapp.utils.extensions

import android.util.Patterns
import dev.erhahahaa.storyapp.data.model.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
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

fun String.toUser(): User {
  val parts = split(",")
  val userId = parts[0]
  val name = parts[1]
  val email = parts[2]
  val token = parts[3]

  return User(userId, name, email, token)
}

fun String.prettifyDate(): String {
  val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
  inputFormat.timeZone = TimeZone.getTimeZone("UTC")

  val outputFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

  return try {
    val date = inputFormat.parse(this)
    val now = Date()

    val diff = now.time - (date?.time ?: 0)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val days = TimeUnit.MILLISECONDS.toDays(diff)

    when {
      days >= 3 -> outputFormat.format(date ?: now)
      days in 1..2 -> "$days day${if (days > 1) "s" else ""} ago"
      hours >= 1 -> "$hours hour${if (hours > 1) "s" else ""} ago"
      else -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
    }
  } catch (e: Exception) {
    e.printStackTrace()
    this
  }
}
