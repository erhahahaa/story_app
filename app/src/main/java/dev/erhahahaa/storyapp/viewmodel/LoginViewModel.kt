package dev.erhahahaa.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.erhahahaa.storyapp.R
import dev.erhahahaa.storyapp.data.model.LoginResponse
import dev.erhahahaa.storyapp.data.repository.UserRepository
import dev.erhahahaa.storyapp.ui.auth.LoginFormState
import dev.erhahahaa.storyapp.utils.extensions.isValidEmail
import dev.erhahahaa.storyapp.utils.extensions.isValidPassword
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

  private val _loginForm = MutableLiveData<LoginFormState>()
  val loginFormState: LiveData<LoginFormState> = _loginForm

  private val _loginResult = MutableLiveData<LoginResponse>()
  val loginResult: LiveData<LoginResponse> = _loginResult

  fun login(email: String, password: String) {
    viewModelScope.launch {
      val result = userRepository.login(email, password)
      _loginResult.postValue(result)
    }
  }

  fun loginDataChanged(email: String, password: String) {
    val state =
      when {
        !email.isValidEmail() -> LoginFormState(emailError = R.string.invalid_email)
        !password.isValidPassword() -> LoginFormState(passwordError = R.string.invalid_password)
        else -> LoginFormState(isDataValid = true)
      }
    _loginForm.postValue(state)
  }
}
