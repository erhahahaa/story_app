package dev.erhahahaa.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.erhahahaa.storyapp.data.model.RegisterResponse
import dev.erhahahaa.storyapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

  private val _registerResult = MutableLiveData<Result<RegisterResponse>>()
  val registerResult: LiveData<Result<RegisterResponse>> = _registerResult

  fun register(name: String, email: String, password: String) {
    viewModelScope.launch {
      val result = userRepository.register(name, email, password)
      _registerResult.value = result
    }
  }
}
