package dev.erhahahaa.storyapp.data.repository

import dev.erhahahaa.storyapp.data.api.ApiService
import dev.erhahahaa.storyapp.data.model.RegisterResponse
import dev.erhahahaa.storyapp.data.model.StoryModel
import dev.erhahahaa.storyapp.data.prefs.UserPreferences
import dev.erhahahaa.storyapp.utils.extensions.asRequestBody
import dev.erhahahaa.storyapp.utils.extensions.toRequestBody
import java.io.File
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody

class StoryRepository
private constructor(
  private val apiService: ApiService,
  private val userPreferences: UserPreferences,
) {

  fun getStories(): Flow<Result<List<StoryModel>>> = flow {
    val token = userPreferences.getToken().first()
    if (token != null) {
      try {
        val response = apiService.getStories("Bearer $token")
        if (!response.error && response.data != null) {
          emit(Result.success(response.data))
        } else {
          emit(Result.failure(Throwable(response.message)))
        }
      } catch (e: Exception) {
        emit(Result.failure(e))
      }
    } else {
      emit(Result.failure(Throwable("User not authenticated")))
    }
  }

  suspend fun addStory(
    file: File,
    description: String,
    lat: Double,
    lon: Double,
  ): Result<RegisterResponse> {
    val token =
      userPreferences.getToken().first()
        ?: return Result.failure(Throwable("User not authenticated"))

    return try {
      val requestBody = description.toRequestBody("text/plain")
      val imagePart = file.asRequestBody("image/jpeg")
      val imageFile = MultipartBody.Part.createFormData("photo", file.name, imagePart)
      val latPart = lat.toString().toRequestBody()
      val lonPart = lon.toString().toRequestBody()

      val response = apiService.addStory("Bearer $token", requestBody, imageFile, latPart, lonPart)
      if (!response.error) {
        Result.success(response)
      } else {
        Result.failure(Throwable(response.message))
      }
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  companion object {
    @Volatile private var instance: StoryRepository? = null

    fun getInstance(apiService: ApiService, userPreferences: UserPreferences): StoryRepository =
      instance ?: synchronized(this) { instance ?: StoryRepository(apiService, userPreferences) }
  }
}
