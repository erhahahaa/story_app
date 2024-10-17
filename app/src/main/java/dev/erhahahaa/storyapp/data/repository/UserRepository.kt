package dev.erhahahaa.storyapp.data.repository

import dev.erhahahaa.storyapp.data.api.ApiService
import dev.erhahahaa.storyapp.data.model.LoginResponse
import dev.erhahahaa.storyapp.data.model.RegisterResponse
import dev.erhahahaa.storyapp.data.prefs.UserPreferences
import kotlinx.coroutines.flow.Flow

class UserRepository
private constructor(
  private val apiService: ApiService,
  private val userPreferences: UserPreferences,
) {

  suspend fun login(email: String, password: String): Result<LoginResponse> {
    return try {
      val response = apiService.login(email, password)
      if (!response.error) {
        Result.success(response)
      } else {
        Result.failure(Throwable(response.message))
      }
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  suspend fun register(name: String, email: String, password: String): Result<RegisterResponse> {
    return try {
      val response = apiService.register(name, email, password)
      if (!response.error) {
        Result.success(response)
      } else {
        Result.failure(Throwable(response.message))
      }
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  fun getToken(): Flow<String?> {
    return userPreferences.getToken()
  }

  suspend fun saveToken(token: String) {
    userPreferences.saveSession(token)
  }

  suspend fun clearSession() {
    userPreferences.clearSession()
  }

  companion object {
    @Volatile private var instance: UserRepository? = null

    fun getInstance(apiService: ApiService, userPreferences: UserPreferences): UserRepository =
      instance ?: synchronized(this) { instance ?: UserRepository(apiService, userPreferences) }
  }
}
