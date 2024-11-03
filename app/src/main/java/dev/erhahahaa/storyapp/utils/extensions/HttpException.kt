package dev.erhahahaa.storyapp.utils.extensions

import android.util.Log
import dev.erhahahaa.storyapp.data.api.ApiConfig
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.HttpException

fun HttpException.parseErrorMessage(): String {
  return try {
    val errorBody = response()?.errorBody()?.string()
    val json = ApiConfig.json.decodeFromString<JsonObject>(errorBody!!)
    json["message"]?.jsonPrimitive?.content ?: "An error occurred"
  } catch (e: Exception) {
    Log.e("HttpException", "parseErrorMessage: $e")
    message ?: "An error occurred"
  }
}
