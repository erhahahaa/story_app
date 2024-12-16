package dev.erhahahaa.storyapp.data.repository

import dev.erhahahaa.storyapp.data.api.ApiService
import dev.erhahahaa.storyapp.data.api.LocationParam
import dev.erhahahaa.storyapp.data.model.EmptyResponse
import dev.erhahahaa.storyapp.data.model.StoriesResponse
import dev.erhahahaa.storyapp.utils.extensions.asRequestBody
import dev.erhahahaa.storyapp.utils.extensions.parseErrorMessage
import dev.erhahahaa.storyapp.utils.extensions.toRequestBody
import java.io.File
import okhttp3.MultipartBody
import retrofit2.HttpException

class StoryRepository private constructor(private val apiService: ApiService) {

  suspend fun getStories(
    token: String,
    page: Int? = null,
    size: Int? = null,
    withLocation: LocationParam? = null,
  ): StoriesResponse {
    return try {
      apiService.getStories(
        bearer = "Bearer $token",
        page = page,
        size = size,
        withLocation = withLocation?.value,
      )
    } catch (e: HttpException) {
      StoriesResponse(true, e.parseErrorMessage(), null)
    }
  }

  suspend fun addStory(
    token: String,
    file: File,
    description: String,
    lat: Double?,
    lon: Double?,
  ): EmptyResponse {
    return try {
      val descBody = description.toRequestBody("text/plain")
      val imagePart = file.asRequestBody("image/jpeg")
      val imageFile = MultipartBody.Part.createFormData("photo", file.name, imagePart)
      val latPart = lat?.toString()?.toRequestBody()
      val lonPart = lon?.toString()?.toRequestBody()

      apiService.addStory(
        bearer = "Bearer $token",
        description = descBody,
        photo = imageFile,
        lat = latPart,
        lon = lonPart,
      )
    } catch (e: HttpException) {
      EmptyResponse(true, e.parseErrorMessage())
    }
  }

  suspend fun addStoryGuest(
    file: File,
    description: String,
    lat: Double?,
    lon: Double?,
  ): EmptyResponse {
    return try {
      val descBody = description.toRequestBody("text/plain")
      val imagePart = file.asRequestBody("image/jpeg")
      val imageFile = MultipartBody.Part.createFormData("photo", file.name, imagePart)
      val latPart = lat?.toString()?.toRequestBody()
      val lonPart = lon?.toString()?.toRequestBody()

      apiService.addStoryGuest(
        description = descBody,
        photo = imageFile,
        lat = latPart,
        lon = lonPart,
      )
    } catch (e: HttpException) {
      EmptyResponse(true, e.parseErrorMessage())
    }
  }

  companion object {
    @Volatile private var instance: StoryRepository? = null

    fun getInstance(apiService: ApiService): StoryRepository =
      instance ?: synchronized(this) { instance ?: StoryRepository(apiService) }
  }
}
