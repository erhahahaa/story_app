package dev.erhahahaa.storyapp.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class UserTest {

  @Test
  fun `test User creation`() {
    val userId = "user123"
    val name = "John Doe"
    val email = "john.doe@example.com"
    val token = "token123"

    val user = User(userId, name, email, token)

    assertNotNull(user)
    assertEquals(userId, user.userId)
    assertEquals(name, user.name)
    assertEquals(email, user.email)
    assertEquals(token, user.token)
  }

  @Test
  fun `test User default values`() {
    val user = User(userId = "", name = "", email = "", token = "")

    assertNotNull(user)
    assertEquals("", user.userId)
    assertEquals("", user.name)
    assertEquals("", user.email)
    assertEquals("", user.token)
  }

  @Test
  fun `test User copy`() {
    val userId = "user123"
    val name = "John Doe"
    val email = "john.doe@example.com"

    val token = "token123"

    val user = User(userId, name, email, token)
    val copiedUser = user.copy()

    assertNotNull(copiedUser)
    assertEquals(userId, copiedUser.userId)
    assertEquals(name, copiedUser.name)
    assertEquals(email, copiedUser.email)
    assertEquals(token, copiedUser.token)
  }

  @Test
  fun `test User toString`() {
    val userId = "user123"
    val name = "John Doe"
    val email = "john.doe@example.com"

    val token = "token123"

    val user = User(userId, name, email, token)
    val expectedString =
      "User(userId=user123, name=John Doe, email=john.doe@example.com, password=password123, token=token123)"

    assertEquals(expectedString, user.toString())
  }
}
