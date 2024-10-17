package dev.erhahahaa.storyapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.erhahahaa.storyapp.data.repository.StoryRepository
import dev.erhahahaa.storyapp.data.repository.UserRepository
import dev.erhahahaa.storyapp.viewmodel.LoginViewModel
import dev.erhahahaa.storyapp.viewmodel.RegisterViewModel
import dev.erhahahaa.storyapp.viewmodel.StoryViewModel

class ViewModelFactory(
  private val userRepository: UserRepository,
  private val storyRepository: StoryRepository,
) : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
      return LoginViewModel(userRepository) as T
    } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
      return RegisterViewModel(userRepository) as T
    } else if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
      return StoryViewModel(storyRepository) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}
