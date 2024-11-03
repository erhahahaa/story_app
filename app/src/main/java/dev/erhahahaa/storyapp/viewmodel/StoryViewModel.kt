package dev.erhahahaa.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.erhahahaa.storyapp.data.model.EmptyResponse
import dev.erhahahaa.storyapp.data.model.StoriesResponse
import dev.erhahahaa.storyapp.data.repository.StoryRepository
import java.io.File
import kotlinx.coroutines.launch

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

  private val _stories = MutableLiveData<StoriesResponse>()
  val stories: LiveData<StoriesResponse> = _stories

  private val _addStoryResult = MutableLiveData<EmptyResponse>()
  val addStoryResult: LiveData<EmptyResponse> = _addStoryResult

  fun getStories(token: String) {
    viewModelScope.launch {
      val result = storyRepository.getStories(token)
      _stories.postValue(result)
    }
  }

  fun addStory(token: String, file: File, description: String, lat: Double, lon: Double) {
    viewModelScope.launch {
      val result = storyRepository.addStory(token, file, description, lat, lon)
      _addStoryResult.value = result
    }
  }
}
