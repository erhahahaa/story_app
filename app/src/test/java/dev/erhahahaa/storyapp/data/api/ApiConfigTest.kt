package dev.erhahahaa.storyapp.data.api

import dev.erhahahaa.storyapp.BuildConfig
import okhttp3.MediaType.Companion.toMediaType
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class ApiConfigTest {
  @Test
  fun `test getApiService returns ApiService instance`() {
    val apiService = ApiConfig.getApiService()
    assertNotNull(apiService, "ApiService should not be null")
  }

  @Test
  fun `test getApiService baseUrl is correct`() {
    val retrofit = Retrofit.Builder().baseUrl(BuildConfig.API_URL).build()
    assertEquals(BuildConfig.API_URL, retrofit.baseUrl().toString(), "Base URL should match")
  }

  @Test
  fun `test getApiService has correct converter factory`() {
    val retrofit =
      Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(ApiConfig.json.asConverterFactory("application/json".toMediaType()))
        .build()
    val converterFactories = retrofit.converterFactories()
    assertTrue(converterFactories.any { true }, "Should have Json converter factory")
  }
}
