package dev.erhahahaa.storyapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import dev.erhahahaa.storyapp.data.model.StoryModel
import dev.erhahahaa.storyapp.data.repository.StoryRepository
import dev.erhahahaa.storyapp.ui.adapter.StoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class StoryViewModelTest {

  @get:Rule val instantExecutorRule = InstantTaskExecutorRule()

  @Mock private lateinit var storyRepository: StoryRepository

  private lateinit var storyViewModel: StoryViewModel
  private val testDispatcher = StandardTestDispatcher()

  private val dummyStories =
    listOf(
      StoryModel("1", "Story 1", "Description 1", "url1", 1.0, 1.0, "2023-01-01T00:00:00Z"),
      StoryModel("2", "Story 2", "Description 2", "url2", 2.0, 2.0, "2023-01-02T00:00:00Z"),
    )

  private val noopListUpdateCallback =
    object : ListUpdateCallback {
      override fun onInserted(position: Int, count: Int) {}

      override fun onRemoved(position: Int, count: Int) {}

      override fun onMoved(fromPosition: Int, toPosition: Int) {}

      override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

  private suspend fun PagingData<StoryModel>.size(): Int {
    val differ =
      AsyncPagingDataDiffer(
        diffCallback = StoryAdapter.STORY_COMPARATOR,
        updateCallback = noopListUpdateCallback,
        mainDispatcher = testDispatcher,
        workerDispatcher = testDispatcher,
      )
    differ.submitData(this)
    return differ.snapshot().items.size
  }

  private suspend fun PagingData<StoryModel>.getFirstItem(): StoryModel? {
    val differ =
      AsyncPagingDataDiffer(
        diffCallback = StoryAdapter.STORY_COMPARATOR,
        updateCallback = noopListUpdateCallback,
        mainDispatcher = testDispatcher,
        workerDispatcher = testDispatcher,
      )
    differ.submitData(this)
    return differ.snapshot().items.firstOrNull()
  }

  @Before
  fun setUp() {
    MockitoAnnotations.openMocks(this)
    Dispatchers.setMain(testDispatcher)
    storyViewModel = StoryViewModel(storyRepository)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `when getStories Should Not Null and Return Success`() = runTest {
    val stories = PagingData.from(dummyStories)
    val expectedStories = MutableLiveData<PagingData<StoryModel>>()
    expectedStories.value = stories

    Mockito.`when`(storyRepository.getStories(Mockito.anyString())).thenReturn(expectedStories)

    val observer = mock<Observer<PagingData<StoryModel>>>()
    storyViewModel.stories.observeForever(observer)

    try {
      storyViewModel.setToken("dummy-token")
      advanceUntilIdle()

      val actualStories = storyViewModel.stories.value
      assertNotNull(actualStories)

      val actualItemCount = actualStories?.size()
      assertEquals(dummyStories.size, actualItemCount)

      val firstItem = actualStories?.getFirstItem()
      assertEquals("Story 1", firstItem?.name)
    } finally {
      storyViewModel.stories.removeObserver(observer)
    }
  }

  @Test
  fun `when getStories Should Return Empty Data`() = runTest {
    val emptyStories = PagingData.from(emptyList<StoryModel>())
    val expectedStories = MutableLiveData<PagingData<StoryModel>>()
    expectedStories.value = emptyStories

    Mockito.`when`(storyRepository.getStories(Mockito.anyString())).thenReturn(expectedStories)

    val observer = mock<Observer<PagingData<StoryModel>>>()
    storyViewModel.stories.observeForever(observer)

    try {
      storyViewModel.setToken("dummy-token")
      advanceUntilIdle()

      val actualStories = storyViewModel.stories.value
      assertNotNull(actualStories)

      val actualItemCount = actualStories?.size()
      assertEquals(0, actualItemCount)

      val firstItem = actualStories?.getFirstItem()
      assertEquals(null, firstItem)
    } finally {
      storyViewModel.stories.removeObserver(observer)
    }
  }
}
