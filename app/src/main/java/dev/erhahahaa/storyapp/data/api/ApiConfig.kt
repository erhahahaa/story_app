package dev.erhahahaa.storyapp.data.api

import dev.erhahahaa.storyapp.BuildConfig
import dev.erhahahaa.storyapp.utils.extensions.toMediaType
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object ApiConfig {
  val json = Json { ignoreUnknownKeys = true }

  fun getApiService(): ApiService {
    return Retrofit.Builder()
      .baseUrl(BuildConfig.API_URL)
      .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
      .build()
      .create(ApiService::class.java)
  }
}
