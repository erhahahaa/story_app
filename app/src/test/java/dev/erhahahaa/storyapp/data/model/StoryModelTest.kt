package dev.erhahahaa.storyapp.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class StoryModelTest {

  @Test
  fun `test StoryModel creation`() {
    val id = "story123"
    val name = "John Doe"
    val description = "A fascinating story"
    val photoUrl = "http://example.com/photo.jpg"
    val createdAt = "2023-01-01T00:00:00Z"
    val lat = 37.7749
    val lon = -122.4194

    val storyModel = StoryModel(id, name, description, photoUrl, lat, lon, createdAt)

    assertNotNull(storyModel)
    assertEquals(id, storyModel.id)
    assertEquals(name, storyModel.name)
    assertEquals(description, storyModel.description)
    assertEquals(photoUrl, storyModel.photoUrl)
    assertEquals(createdAt, storyModel.createdAt)
    storyModel.lat?.let { assertEquals(lat, it, 0.0) }
    storyModel.lon?.let { assertEquals(lon, it, 0.0) }
  }

  @Test
  fun `test StoryModel default values`() {
    val storyModel =
      StoryModel(
        id = "",
        name = "",
        description = "",
        photoUrl = "",
        lat = 0.0,
        lon = 0.0,
        createdAt = "",
      )

    assertNotNull(storyModel)
    assertEquals("", storyModel.id)
    assertEquals("", storyModel.name)
    assertEquals("", storyModel.description)
    assertEquals("", storyModel.photoUrl)
    assertEquals("", storyModel.createdAt)
    storyModel.lat?.let { assertEquals(0.0, it, 0.0) }
    storyModel.lon?.let { assertEquals(0.0, it, 0.0) }
  }

  @Test
  fun `test StoryModel copy`() {
    val id = "story123"
    val name = "John Doe"
    val description = "A fascinating story"
    val photoUrl = "http://example.com/photo.jpg"
    val createdAt = "2023-01-01T00:00:00Z"
    val lat = 37.7749
    val lon = -122.4194

    val storyModel = StoryModel(id, name, description, photoUrl, lat, lon, createdAt)
    val copiedStoryModel = storyModel.copy()

    assertNotNull(copiedStoryModel)
    assertEquals(id, copiedStoryModel.id)
    assertEquals(name, copiedStoryModel.name)
    assertEquals(description, copiedStoryModel.description)
    assertEquals(photoUrl, copiedStoryModel.photoUrl)
    assertEquals(createdAt, copiedStoryModel.createdAt)
    copiedStoryModel.lat?.let { assertEquals(lat, it, 0.0) }
    copiedStoryModel.lon?.let { assertEquals(lon, it, 0.0) }
  }
}
