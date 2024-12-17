package dev.erhahahaa.storyapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import dev.erhahahaa.storyapp.data.api.LocationParam
import dev.erhahahaa.storyapp.data.model.StoriesResponse
import dev.erhahahaa.storyapp.data.model.StoryModel
import dev.erhahahaa.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
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
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class StoryViewModelTest {

  @get:Rule val instantExecutorRule = InstantTaskExecutorRule()

  @Mock private lateinit var storyRepository: StoryRepository

  private lateinit var storyViewModel: StoryViewModel

  @Mock private lateinit var observer: Observer<StoriesResponse?>

  private val testDispatcher = StandardTestDispatcher()

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
    MockitoAnnotations.openMocks(this)
    storyViewModel = StoryViewModel(storyRepository)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `when getStoriesWithLocation Should Not Null and Return Success`() =
    runTest(testDispatcher) {
      val dummyStories =
        listOf(
          StoryModel("1", "Story 1", "Description 1", "url1", 1.0, 1.0, "2023-01-01T00:00:00Z"),
          StoryModel("2", "Story 2", "Description 2", "url2", 2.0, 2.0, "2023-01-02T00:00:00Z"),
        )
      val expectedStories = StoriesResponse(false, "", dummyStories)
      Mockito.`when`(
          storyRepository.getStories(
            Mockito.anyString(),
            Mockito.isNull(),
            Mockito.isNull(),
            Mockito.eq(LocationParam.WITH_LOCATION),
          )
        )
        .thenReturn(expectedStories)

      storyViewModel.storiesWithLocation.observeForever(observer)
      storyViewModel.getStoriesWithLocation("token")
      testDispatcher.scheduler.advanceUntilIdle()

      verify(observer).onChanged(expectedStories)
      assertNotNull(storyViewModel.storiesWithLocation.value)
      assertEquals(expectedStories, storyViewModel.storiesWithLocation.value)
      assertEquals(expectedStories.data?.size, storyViewModel.storiesWithLocation.value?.data?.size)
      assertEquals("Story 1", storyViewModel.storiesWithLocation.value?.data?.first()?.name)
    }

  @Test
  fun `when getStoriesWithLocation Should Return Empty Data`() =
    runTest(testDispatcher) {
      val expectedStories = StoriesResponse(false, "", emptyList())
      Mockito.`when`(
          storyRepository.getStories(
            Mockito.anyString(),
            Mockito.isNull(),
            Mockito.isNull(),
            Mockito.eq(LocationParam.WITH_LOCATION),
          )
        )
        .thenReturn(expectedStories)

      storyViewModel.storiesWithLocation.observeForever(observer)
      storyViewModel.getStoriesWithLocation("token")
      testDispatcher.scheduler.advanceUntilIdle()

      verify(observer).onChanged(expectedStories)
      assertNotNull(storyViewModel.storiesWithLocation.value)
      assertEquals(expectedStories, storyViewModel.storiesWithLocation.value)
      assertEquals(expectedStories.data?.size, storyViewModel.storiesWithLocation.value?.data?.size)
    }
}
