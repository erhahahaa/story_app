package dev.erhahahaa.storyapp.data.api

import dev.erhahahaa.storyapp.data.model.EmptyResponse
import dev.erhahahaa.storyapp.data.model.LoginResponse
import dev.erhahahaa.storyapp.data.model.RegisterResponse
import dev.erhahahaa.storyapp.data.model.StoriesResponse
import dev.erhahahaa.storyapp.data.model.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

enum class LocationParam(val value: Int) {
  WITH_LOCATION(1),
  WITHOUT_LOCATION(0),
}

interface ApiService {
  @FormUrlEncoded
  @POST("register")
  suspend fun register(
    @Field("name") name: String,
    @Field("email") email: String,
    @Field("password") password: String,
  ): RegisterResponse

  @FormUrlEncoded
  @POST("login")
  suspend fun login(
    @Field("email") email: String,
    @Field("password") password: String,
  ): LoginResponse

  @Multipart
  @POST("stories")
  suspend fun addStory(
    @Header("Authorization") bearer: String,
    @Part("description") description: RequestBody,
    @Part photo: MultipartBody.Part,
    @Part("lat") lat: RequestBody?,
    @Part("lon") lon: RequestBody?,
  ): EmptyResponse

  @Multipart
  @POST("stories/guest")
  suspend fun addStoryGuest(
    @Part("description") description: RequestBody,
    @Part photo: MultipartBody.Part,
    @Part("lat") lat: RequestBody?,
    @Part("lon") lon: RequestBody?,
  ): EmptyResponse

  @GET("stories")
  suspend fun getStories(
    @Header("Authorization") bearer: String,
    @Query("page") page: Int? = null,
    @Query("size") size: Int? = null,
    @Query("location") withLocation: Int? = null,
  ): StoriesResponse

  @GET("stories/{id}")
  suspend fun getStoryById(
    @Header("Authorization") bearer: String,
    @Path("id") id: String,
  ): StoryResponse
}
