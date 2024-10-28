package dev.erhahahaa.storyapp.data.repository

import dev.erhahahaa.storyapp.data.api.ApiService
import dev.erhahahaa.storyapp.data.model.LoginResponse
import dev.erhahahaa.storyapp.data.model.RegisterResponse
import dev.erhahahaa.storyapp.data.prefs.UserPreferences
import dev.erhahahaa.storyapp.utils.extensions.parseError
import retrofit2.HttpException

class UserRepository
private constructor(
  private val apiService: ApiService,
  private val userPreferences: UserPreferences,
) {

  suspend fun login(email: String, password: String): LoginResponse {
    return try {
      val res = apiService.login(email, password)
      if (!res.error) {
        res.data?.let { userPreferences.saveSession(it.token) }
      }
      res
    } catch (e: HttpException) {
      e.parseError<LoginResponse>()
    }
  }

  suspend fun register(name: String, email: String, password: String): RegisterResponse {
    return try {
      apiService.register(name, email, password)
    } catch (e: HttpException) {
      e.parseError<RegisterResponse>()
    }
  }

  suspend fun getToken(): String? {
    return userPreferences.getToken()
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
