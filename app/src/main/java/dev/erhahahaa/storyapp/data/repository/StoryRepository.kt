package dev.erhahahaa.storyapp.data.repository

import dev.erhahahaa.storyapp.data.api.ApiService
import dev.erhahahaa.storyapp.data.model.EmptyResponse
import dev.erhahahaa.storyapp.data.model.StoriesResponse
import dev.erhahahaa.storyapp.data.prefs.UserPreferences
import dev.erhahahaa.storyapp.utils.extensions.asRequestBody
import dev.erhahahaa.storyapp.utils.extensions.parseError
import dev.erhahahaa.storyapp.utils.extensions.toRequestBody
import java.io.File
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import retrofit2.HttpException

class StoryRepository
private constructor(
  private val apiService: ApiService,
  private val userPreferences: UserPreferences,
) {

  private var token: String? = runBlocking { userPreferences.getToken() }

  suspend fun getStories(): StoriesResponse {
    return try {
      apiService.getStories("Bearer $token")
    } catch (e: HttpException) {
      e.parseError<StoriesResponse>()
    }
  }

  suspend fun addStory(file: File, description: String, lat: Double, lon: Double): EmptyResponse {
    if (token == null) return EmptyResponse.unauthenticated

    return try {
      val requestBody = description.toRequestBody("text/plain")
      val imagePart = file.asRequestBody("image/jpeg")
      val imageFile = MultipartBody.Part.createFormData("photo", file.name, imagePart)
      val latPart = lat.toString().toRequestBody()
      val lonPart = lon.toString().toRequestBody()
      apiService.addStory("Bearer $token", requestBody, imageFile, latPart, lonPart)
    } catch (e: HttpException) {
      e.parseError<EmptyResponse>()
    }
  }

  companion object {
    @Volatile private var instance: StoryRepository? = null

    fun getInstance(apiService: ApiService, userPreferences: UserPreferences): StoryRepository =
      instance ?: synchronized(this) { instance ?: StoryRepository(apiService, userPreferences) }
  }
}
