package dev.erhahahaa.storyapp.utils

import dev.erhahahaa.storyapp.data.model.ApiResponse
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class ApiResponseSerializer<T>(private val dataSerializer: KSerializer<T>) :
  KSerializer<ApiResponse<T>> {

  override val descriptor = ApiResponse.serializer(dataSerializer).descriptor

  override fun serialize(encoder: Encoder, value: ApiResponse<T>) {
    ApiResponse.serializer(dataSerializer).serialize(encoder, value)
  }

  override fun deserialize(decoder: Decoder): ApiResponse<T> {
    if (decoder !is JsonDecoder) {
      throw SerializationException("Expected JsonDecoder")
    }

    val jsonObject = decoder.decodeJsonElement().jsonObject

    val error = jsonObject["error"]?.jsonPrimitive?.boolean ?: true
    val message = jsonObject["message"]?.jsonPrimitive?.content ?: "Unknown error"

    val dataKey = jsonObject.keys.firstOrNull { it != "error" && it != "message" }
    val dataValue = dataKey?.let { jsonObject[it] }

    val data = dataValue?.let { decoder.json.decodeFromJsonElement(dataSerializer, it) }

    return ApiResponse(error, message, data)
  }
}
