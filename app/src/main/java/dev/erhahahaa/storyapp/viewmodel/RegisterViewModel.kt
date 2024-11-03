package dev.erhahahaa.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.erhahahaa.storyapp.R
import dev.erhahahaa.storyapp.data.model.RegisterResponse
import dev.erhahahaa.storyapp.data.repository.UserRepository
import dev.erhahahaa.storyapp.ui.auth.RegisterFormState
import dev.erhahahaa.storyapp.utils.extensions.isValidEmail
import dev.erhahahaa.storyapp.utils.extensions.isValidName
import dev.erhahahaa.storyapp.utils.extensions.isValidPassword
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

  private val _registerForm = MutableLiveData<RegisterFormState>()
  val registerFormState: LiveData<RegisterFormState> = _registerForm

  private val _registerResult = MutableLiveData<RegisterResponse>()
  val registerResult: LiveData<RegisterResponse> = _registerResult

  fun register(name: String, email: String, password: String) {
    viewModelScope.launch {
      val result = userRepository.register(name, email, password)
      _registerResult.postValue(result)
    }
  }

  fun registerDataChanged(name: String, email: String, password: String) {
    val state =
      when {
        !name.isValidName() -> RegisterFormState(nameError = R.string.invalid_name)
        !email.isValidEmail() -> RegisterFormState(emailError = R.string.invalid_email)
        !password.isValidPassword() -> RegisterFormState(passwordError = R.string.invalid_password)
        else -> RegisterFormState(isDataValid = true)
      }
    _registerForm.postValue(state)
  }
}
