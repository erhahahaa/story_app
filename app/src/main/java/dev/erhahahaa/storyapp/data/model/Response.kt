package dev.erhahahaa.storyapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class BaseResponse {
  abstract val error: Boolean
  abstract val message: String
}

@Serializable
data class EmptyResponse(override val error: Boolean, override val message: String) :
  BaseResponse() {
  companion object {
    val unauthenticated: EmptyResponse = EmptyResponse(error = true, message = "Unauthenticated")
  }
}

typealias RegisterResponse = EmptyResponse

@Serializable
data class LoginResponse(
  override val error: Boolean,
  override val message: String,
  @SerialName("loginResult") val data: LoginResult?,
) : BaseResponse()

@Serializable
data class StoryResponse(
  override val error: Boolean,
  override val message: String,
  @SerialName("story") val data: StoryModel?,
) : BaseResponse()

@Serializable
data class StoriesResponse(
  override val error: Boolean,
  override val message: String,
  @SerialName("listStory") val data: List<StoryModel>?,
) : BaseResponse()
