package dev.erhahahaa.storyapp.data.model

import kotlinx.serialization.Serializable

@Serializable data class ApiResponse<T>(val error: Boolean, val message: String, val data: T?)

typealias LoginResponse = ApiResponse<LoginResult>

typealias RegisterResponse = ApiResponse<Unit>

typealias StoriesResponse = ApiResponse<List<StoryModel>>

typealias StoryResponse = ApiResponse<StoryModel>

typealias EmptyResponse = ApiResponse<Unit>
