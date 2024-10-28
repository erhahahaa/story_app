package dev.erhahahaa.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.erhahahaa.storyapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {
  private val _token = MutableLiveData<String?>()
  val token: LiveData<String?> = _token

  init {
    getToken()
  }

  fun getToken() {
    viewModelScope.launch {
      val result = userRepository.getToken()
      _token.postValue(result)
    }
  }
}
