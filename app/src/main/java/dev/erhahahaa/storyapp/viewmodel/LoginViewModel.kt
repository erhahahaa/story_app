package dev.erhahahaa.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.erhahahaa.storyapp.data.model.LoginResponse
import dev.erhahahaa.storyapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

  private val _loginResult = MutableLiveData<Result<LoginResponse>>()
  val loginResult: LiveData<Result<LoginResponse>> = _loginResult

  fun login(email: String, password: String) {
    viewModelScope.launch {
      val result = userRepository.login(email, password)
      _loginResult.value = result
    }
  }

  fun saveToken(token: String) {
    viewModelScope.launch { userRepository.saveToken(token) }
  }
}
