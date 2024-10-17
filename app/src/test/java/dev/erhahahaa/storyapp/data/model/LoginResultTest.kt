package dev.erhahahaa.storyapp.data.model

import kotlinx.serialization.json.Json
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue

class LoginResultTest {
  @Test
  fun `test LoginResult serialization`() {
    val loginResult =
      LoginResult(userId = "asdf-asdf-asdf-asdf", name = "Test User", email = "john@gmail.com")
    val json = Json.encodeToString(LoginResult.serializer(), loginResult)
    assertNotNull(json)
    assertTrue(json.contains("Test User"))
  }

  @Test
  fun `test LoginResult deserialization`() {
    val json =
      """
            {
                "userId": "asdf-asdf-asdf-asdf",
                "name": "Test User",
                "email": "john@gmail.com"
            }
  """
    val loginResult = Json.decodeFromString(LoginResult.serializer(), json)
    assertNotNull(loginResult)
    assertEquals("Test User", loginResult.name)
  }
}
