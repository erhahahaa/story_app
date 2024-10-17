package dev.erhahahaa.storyapp.data.model

import kotlinx.serialization.json.Json
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class StoryModelTest {
  @Test
  fun `test StoryModel serialization`() {
    val story =
      StoryModel(
        id = "asdf-asdf-asdf-asdf",
        name = "Test Story",
        description = "This is a test story",
        photoUrl = "http://example.com/photo.jpg",
        lat = 12.34,
        lon = 56.78,
        createdAt = "2023-10-01T12:00:00Z",
      )
    val json = Json.encodeToString(StoryModel.serializer(), story)
    assertNotNull(json)
    assertTrue(json.contains("Test Story"))
  }

  @Test
  fun `test StoryModel deserialization`() {
    val json =
      """
            {
                "id": "asdf-asdf-asdf-asdf",
                "name": "Test Story",
                "description": "This is a test story",
                "photoUrl": "http://example.com/photo.jpg",
                "lat": 12.34,
                "lon": 56.78,
                "createdAt": "2023-10-01T12:00:00Z"
            }
        """
    val story = Json.decodeFromString(StoryModel.serializer(), json)
    assertNotNull(story)
    assertEquals("Test Story", story.name)
  }
}
