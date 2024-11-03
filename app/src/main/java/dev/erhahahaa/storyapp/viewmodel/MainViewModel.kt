package dev.erhahahaa.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.erhahahaa.storyapp.data.model.User
import dev.erhahahaa.storyapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {
  private val _user = MutableLiveData<User?>()
  val user: LiveData<User?> = _user

  init {
    getUser()
  }

  fun getUser() {
    viewModelScope.launch {
      val result = userRepository.getUser()
      _user.postValue(result)
    }
  }

  fun logout() {
    viewModelScope.launch {
      userRepository.clearUser()
      _user.postValue(null)
    }
  }
}
