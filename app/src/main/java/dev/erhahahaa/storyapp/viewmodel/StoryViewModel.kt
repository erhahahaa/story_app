package dev.erhahahaa.storyapp.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.erhahahaa.storyapp.R
import dev.erhahahaa.storyapp.data.api.LocationParam
import dev.erhahahaa.storyapp.data.model.EmptyResponse
import dev.erhahahaa.storyapp.data.model.StoriesResponse
import dev.erhahahaa.storyapp.data.model.StoryModel
import dev.erhahahaa.storyapp.data.repository.StoryRepository
import dev.erhahahaa.storyapp.ui.story.StoryFormState
import java.io.File
import kotlinx.coroutines.launch

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

  private val _storiesWithLocation = MutableLiveData<StoriesResponse?>()
  val storiesWithLocation: LiveData<StoriesResponse?> = _storiesWithLocation
  private val _token = MutableLiveData<String>()

  val stories: LiveData<PagingData<StoryModel>> =
    _token.switchMap { token -> storyRepository.getStories(token).cachedIn(viewModelScope) }

  fun setToken(token: String) {
    _token.value = token
  }

  private val _storyForm = MutableLiveData<StoryFormState?>()
  val storyFormState: LiveData<StoryFormState?> = _storyForm

  private val _addStoryResult = MutableLiveData<EmptyResponse?>()
  val addStoryResult: LiveData<EmptyResponse?> = _addStoryResult

  private val _isFinishAddStory = MutableLiveData<Boolean>()
  val isLoading: LiveData<Boolean> = _isFinishAddStory

  fun getStoriesWithLocation(token: String) {
    viewModelScope.launch {
      val result = storyRepository.getStories(token, null, null, LocationParam.WITH_LOCATION)
      _storiesWithLocation.postValue(result)
    }
  }

  fun addStory(token: String, file: File, description: String, lat: Double?, lon: Double?) {
    viewModelScope.launch {
      _isFinishAddStory.postValue(true)
      val result = storyRepository.addStory(token, file, description, lat, lon)
      _isFinishAddStory.postValue(false)
      _addStoryResult.postValue(result)
    }
  }

  fun addStoryGuest(file: File, description: String, lat: Double?, lon: Double?) {
    viewModelScope.launch {
      _isFinishAddStory.postValue(true)
      val result = storyRepository.addStoryGuest(file, description, lat, lon)
      _isFinishAddStory.postValue(false)
      _addStoryResult.postValue(result)
    }
  }

  fun storyDataChanged(description: String, imageUri: File?, location: Location?) {
    val state =
      when {
        description.isEmpty() -> StoryFormState(descriptionError = R.string.invalid_description)
        imageUri == null -> StoryFormState(photoError = R.string.invalid_image)
        location != null && location.latitude == 0.0 && location.longitude == 0.0 ->
          StoryFormState(locationError = R.string.invalid_location)
        else -> StoryFormState(isDataValid = true)
      }
    _storyForm.postValue(state)
  }

  fun clearResult() {
    _addStoryResult.postValue(null)
  }
}
