package dev.erhahahaa.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.erhahahaa.storyapp.data.model.RegisterResponse
import dev.erhahahaa.storyapp.data.model.StoryModel
import dev.erhahahaa.storyapp.data.repository.StoryRepository
import java.io.File
import kotlinx.coroutines.launch

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

  private val _stories = MutableLiveData<Result<List<StoryModel>>>()
  val stories: LiveData<Result<List<StoryModel>>> = _stories

  private val _addStoryResult = MutableLiveData<Result<RegisterResponse>>()
  val addStoryResult: LiveData<Result<RegisterResponse>> = _addStoryResult

  fun getStories() {
    viewModelScope.launch { storyRepository.getStories().collect { _stories.value = it } }
  }

  fun addStory(file: File, description: String, lat: Double, lon: Double) {
    viewModelScope.launch {
      val result = storyRepository.addStory(file, description, lat, lon)
      _addStoryResult.value = result
    }
  }
}
