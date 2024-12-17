package dev.erhahahaa.storyapp.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class LoginResultTest {

  @Test
  fun `test LoginResult creation`() {
    val userId = "user123"
    val name = "John Doe"
    val token = "token123"

    val loginResult = LoginResult(userId, name, token)

    assertNotNull(loginResult)
    assertEquals(userId, loginResult.userId)
    assertEquals(name, loginResult.name)
    assertEquals(token, loginResult.token)
  }

  @Test
  fun `test LoginResult default values`() {
    val loginResult = LoginResult(userId = "", name = "", token = "")

    assertNotNull(loginResult)
    assertEquals("", loginResult.userId)
    assertEquals("", loginResult.name)
    assertEquals("", loginResult.token)
  }

  @Test
  fun `test LoginResult copy`() {
    val userId = "user123"
    val name = "John Doe"
    val token = "token123"

    val loginResult = LoginResult(userId, name, token)
    val copiedLoginResult = loginResult.copy()

    assertNotNull(copiedLoginResult)
    assertEquals(userId, copiedLoginResult.userId)
    assertEquals(name, copiedLoginResult.name)
    assertEquals(token, copiedLoginResult.token)
  }

  @Test
  fun `test LoginResult toString`() {
    val userId = "user123"
    val name = "John Doe"
    val token = "token123"

    val loginResult = LoginResult(userId, name, token)
    val expectedString = "LoginResult(userId=user123, name=John Doe, token=token123)"

    assertEquals(expectedString, loginResult.toString())
  }
}
