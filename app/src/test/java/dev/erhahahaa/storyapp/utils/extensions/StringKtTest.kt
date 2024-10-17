package dev.erhahahaa.storyapp.utils.extensions

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.File

class StringKtTest {
  @Test
  fun `test toMediaType with invalid type`() {
    val result = "invalid/type".toMediaType()
    assertEquals("invalid/type", result.toString())
  }

  @Test
  fun `test toRequestBody with different content type`() {
    val result = "sample text".toRequestBody("application/json")
    assertEquals("application/json; charset=utf-8", result.contentType().toString())
  }

  @Test
  fun `test toRequestBody with empty content type`() {
    val result = "".toRequestBody()
    assertEquals("text/plain; charset=utf-8", result.contentType().toString())
  }

  @Test
  fun `test asRequestBody with different content type`() {
    val file = File("test.jpg")
    val result = file.asRequestBody("application/octet-stream")
    assertEquals("application/octet-stream", result.contentType().toString())
  }

  @Test
  fun `test asRequestBody with non-existent file`() {
    val file = File("non_existent.jpg")
    val result = file.asRequestBody()
    assertEquals("image/jpeg", result.contentType().toString())
  }

  @Test
  fun `test asRequestBody with empty content type`() {
    val result = "application/json".toMediaType()
    assertEquals("application/json", result.toString())
  }

  @Test
  fun `test toRequestBody with default content type`() {
    val result = "sample text".toRequestBody()
    assertEquals("text/plain; charset=utf-8", result.contentType().toString())
  }
}
