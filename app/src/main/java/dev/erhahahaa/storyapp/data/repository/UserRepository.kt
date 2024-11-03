package dev.erhahahaa.storyapp.data.repository

import dev.erhahahaa.storyapp.data.api.ApiService
import dev.erhahahaa.storyapp.data.model.LoginResponse
import dev.erhahahaa.storyapp.data.model.RegisterResponse
import dev.erhahahaa.storyapp.data.model.User
import dev.erhahahaa.storyapp.data.prefs.UserPreferences
import dev.erhahahaa.storyapp.utils.extensions.parseErrorMessage
import retrofit2.HttpException

class UserRepository
private constructor(
  private val apiService: ApiService,
  private val userPreferences: UserPreferences,
) {

  suspend fun login(email: String, password: String): LoginResponse {
    return try {
      val result = apiService.login(email, password)
      if (!result.error) {
        result.data?.let {
          if (it.token.isNotEmpty()) userPreferences.clearUser()
          userPreferences.saveUser(User(it.userId, it.name, email, it.token))
        }
      }
      result
    } catch (e: HttpException) {
      LoginResponse(true, e.parseErrorMessage(), null)
    }
  }

  suspend fun register(name: String, email: String, password: String): RegisterResponse {
    return try {
      apiService.register(name, email, password)
    } catch (e: HttpException) {
      RegisterResponse(true, e.parseErrorMessage())
    }
  }

  suspend fun getUser(): User? {
    return userPreferences.getUser()
  }

  suspend fun clearUser() {
    userPreferences.clearUser()
  }

  companion object {
    @Volatile private var instance: UserRepository? = null

    fun getInstance(apiService: ApiService, userPreferences: UserPreferences): UserRepository =
      instance ?: synchronized(this) { instance ?: UserRepository(apiService, userPreferences) }
  }
}
