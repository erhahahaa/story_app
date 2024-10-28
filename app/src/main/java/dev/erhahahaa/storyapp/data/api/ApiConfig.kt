package dev.erhahahaa.storyapp.data.api

import android.util.Log
import dev.erhahahaa.storyapp.BuildConfig
import java.util.concurrent.TimeUnit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object ApiConfig {
  val json: Json by lazy { Json { ignoreUnknownKeys = true } }

  private fun getLoggingInterceptor(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor { message -> Log.d("ApiLog", message) }
    logging.level =
      if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
      else HttpLoggingInterceptor.Level.NONE
    return logging
  }

  private fun getOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
      .addInterceptor(getLoggingInterceptor())
      .connectTimeout(30, TimeUnit.SECONDS)
      .readTimeout(30, TimeUnit.SECONDS)
      .build()
  }

  fun getApiService(): ApiService {
    return Retrofit.Builder()
      .baseUrl(BuildConfig.API_URL)
      .client(getOkHttpClient())
      .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
      .build()
      .create(ApiService::class.java)
  }
}
