package dev.erhahahaa.storyapp.utils.extensions

import dev.erhahahaa.storyapp.data.api.ApiConfig
import dev.erhahahaa.storyapp.data.model.BaseResponse
import dev.erhahahaa.storyapp.data.model.EmptyResponse
import retrofit2.HttpException

inline fun <reified T : BaseResponse> HttpException.parseError(): T {
  return try {
    val errorBody = response()?.errorBody()?.string()
    ApiConfig.json.decodeFromString<T>(errorBody!!)
  } catch (e: Exception) {
    EmptyResponse(true, e.message ?: "An error occurred") as T
  }
}
