package dev.erhahahaa.storyapp.utils.extensions

import android.content.Context
import dev.erhahahaa.storyapp.data.api.ApiConfig
import dev.erhahahaa.storyapp.data.prefs.UserPreferences
import dev.erhahahaa.storyapp.data.repository.StoryRepository
import dev.erhahahaa.storyapp.data.repository.UserRepository
import dev.erhahahaa.storyapp.di.ViewModelFactory

fun Context.getViewModelFactory(): ViewModelFactory {
  val apiService = ApiConfig.getApiService()
  val userPref = UserPreferences.getInstance(this)
  val userRepo = UserRepository.getInstance(apiService, userPref)
  val storyRepo = StoryRepository.getInstance(apiService, userPref)
  return ViewModelFactory(userRepo, storyRepo)
}
