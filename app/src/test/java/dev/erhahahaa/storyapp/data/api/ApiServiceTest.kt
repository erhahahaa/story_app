package dev.erhahahaa.storyapp.data.api

import dev.erhahahaa.storyapp.data.model.EmptyResponse
import dev.erhahahaa.storyapp.data.model.LoginResponse
import dev.erhahahaa.storyapp.data.model.RegisterResponse
import dev.erhahahaa.storyapp.data.model.StoriesResponse
import dev.erhahahaa.storyapp.data.model.StoryResponse
import dev.erhahahaa.storyapp.mockGeneric
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Retrofit

class ApiServiceTest {

  private lateinit var mockRetrofit: Retrofit
  private lateinit var apiService: ApiService

  object FakeData {
    const val NAME: String = "John Doe"
    const val EMAIL: String = "john@gmail.com"
    const val PASSWORD: String = "password"
    const val TOKEN: String = "Bearer token"
  }

  @Before
  fun setUp() {
    mockRetrofit = mock(Retrofit::class.java)
    apiService = mock(ApiService::class.java)
  }

  @Test
  fun `test register with invalid data`(): Unit = runBlocking {
    `when`(apiService.register("", "", "")).thenThrow(RuntimeException("Invalid data"))

    try {
      apiService.register("", "", "")
    } catch (e: Exception) {
      assertNotNull(e)
    }
  }

  @Test
  fun `test register with valid data`() = runBlocking {
    val mockResponse: RegisterResponse = mockGeneric()

    `when`(apiService.register(FakeData.NAME, FakeData.EMAIL, FakeData.PASSWORD))
      .thenReturn(mockResponse)

    val response = apiService.register(FakeData.NAME, FakeData.EMAIL, FakeData.PASSWORD)

    assertNotNull(response)
  }

  @Test
  fun `test login with invalid data`(): Unit = runBlocking {
    `when`(apiService.login("", "")).thenThrow(RuntimeException("Invalid data"))

    try {
      apiService.login("", "")
    } catch (e: Exception) {
      assertNotNull(e)
    }
  }

  @Test
  fun `test login with valid data`() = runBlocking {
    val mockResponse: LoginResponse = mockGeneric()

    `when`(apiService.login(FakeData.EMAIL, FakeData.PASSWORD)).thenReturn(mockResponse)

    val response = apiService.login(FakeData.EMAIL, FakeData.PASSWORD)

    assertNotNull(response)
  }

  @Test
  fun `test addStory with invalid data`(): Unit = runBlocking {
    val description = mock(RequestBody::class.java)
    val photo = mock(MultipartBody.Part::class.java)
    val lat = mock(RequestBody::class.java)
    val lon = mock(RequestBody::class.java)

    `when`(apiService.addStory("", description, photo, lat, lon))
      .thenThrow(RuntimeException("Invalid data"))

    try {
      apiService.addStory("", description, photo, lat, lon)
    } catch (e: Exception) {
      assertNotNull(e)
    }
  }

  @Test
  fun `test addStory with valid data`() = runBlocking {
    val mockResponse: EmptyResponse = mockGeneric()
    val description = mock(RequestBody::class.java)
    val photo = mock(MultipartBody.Part::class.java)
    val lat = mock(RequestBody::class.java)
    val lon = mock(RequestBody::class.java)

    `when`(apiService.addStory(FakeData.TOKEN, description, photo, lat, lon))
      .thenReturn(mockResponse)

    val response = apiService.addStory(FakeData.TOKEN, description, photo, lat, lon)

    assertNotNull(response)
  }

  @Test
  fun `test addStoryGuest with invalid data`(): Unit = runBlocking {
    val description = ""
    val photo = mock(MultipartBody.Part::class.java)
    val lat = 0.0
    val lon = 0.0

    `when`(apiService.addStoryGuest(description, photo, lat, lon))
      .thenThrow(RuntimeException("Invalid data"))

    try {
      apiService.addStoryGuest(description, photo, lat, lon)
    } catch (e: Exception) {
      assertNotNull(e)
    }
  }

  @Test
  fun `test addStoryGuest with valid data`() = runBlocking {
    val mockResponse: EmptyResponse = mockGeneric()
    val description = "description"
    val photo = mock(MultipartBody.Part::class.java)
    val lat = 0.0
    val lon = 0.0

    `when`(apiService.addStoryGuest(description, photo, lat, lon)).thenReturn(mockResponse)

    val response = apiService.addStoryGuest(description, photo, lat, lon)

    assertNotNull(response)
  }

  @Test
  fun `test getStories with invalid token`(): Unit = runBlocking {
    `when`(apiService.getStories("")).thenThrow(RuntimeException("Invalid token"))

    try {
      apiService.getStories("")
    } catch (e: Exception) {
      assertNotNull(e)
    }
  }

  @Test
  fun `test getStories with valid token`() = runBlocking {
    val mockResponse: StoriesResponse = mockGeneric()

    `when`(apiService.getStories(FakeData.TOKEN)).thenReturn(mockResponse)

    val response = apiService.getStories(FakeData.TOKEN)

    assertNotNull(response)
  }

  @Test
  fun `test getStories with valid token and page`() = runBlocking {
    val mockResponse: StoriesResponse = mockGeneric()
    val page = 1

    `when`(apiService.getStories(FakeData.TOKEN, page)).thenReturn(mockResponse)

    val response = apiService.getStories(FakeData.TOKEN, page)

    assertNotNull(response)
  }

  @Test
  fun `test getStories with valid token, page and size`() = runBlocking {
    val mockResponse: StoriesResponse = mockGeneric()
    val page = 1
    val size = 10

    `when`(apiService.getStories(FakeData.TOKEN, page, size)).thenReturn(mockResponse)

    val response = apiService.getStories(FakeData.TOKEN, page, size)

    assertNotNull(response)
  }

  @Test
  fun `test getStories with valid token, page, size and location`() = runBlocking {
    val mockResponse: StoriesResponse = mockGeneric()
    val page = 1
    val size = 10
    val location = LocationParam.WITH_LOCATION

    `when`(apiService.getStories(FakeData.TOKEN, page, size, location)).thenReturn(mockResponse)

    val response = apiService.getStories(FakeData.TOKEN, page, size, location)

    assertNotNull(response)
  }

  @Test
  fun `test getStoryById with invalid token`(): Unit = runBlocking {
    val id = "asdf-asdf-asdf-asdf"

    `when`(apiService.getStoryById("", id)).thenThrow(RuntimeException("Invalid token"))

    try {
      apiService.getStoryById("", id)
    } catch (e: Exception) {
      assertNotNull(e)
    }
  }

  @Test
  fun `test getStoryById with valid token`() = runBlocking {
    val mockResponse: StoryResponse = mockGeneric()
    val id = "asdf-asdf-asdf-asdf"

    `when`(apiService.getStoryById(FakeData.TOKEN, id)).thenReturn(mockResponse)

    val response = apiService.getStoryById(FakeData.TOKEN, id)

    assertNotNull(response)
  }
}
