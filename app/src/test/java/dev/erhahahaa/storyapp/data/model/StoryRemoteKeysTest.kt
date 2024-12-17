package dev.erhahahaa.storyapp.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class StoryRemoteKeysTest {

  @Test
  fun `test StoryRemoteKeys creation`() {
    val id = "story123"
    val prevKey = 1
    val nextKey = 3

    val storyRemoteKeys = StoryRemoteKeys(id, prevKey, nextKey)

    assertNotNull(storyRemoteKeys)
    assertEquals(id, storyRemoteKeys.storyId)
    assertEquals(prevKey, storyRemoteKeys.prevKey)
    assertEquals(nextKey, storyRemoteKeys.nextKey)
  }

  @Test
  fun `test StoryRemoteKeys default values`() {
    val storyRemoteKeys = StoryRemoteKeys(storyId = "", prevKey = null, nextKey = null)

    assertNotNull(storyRemoteKeys)
    assertEquals("", storyRemoteKeys.storyId)
    assertEquals(null, storyRemoteKeys.prevKey)
    assertEquals(null, storyRemoteKeys.nextKey)
  }

  @Test
  fun `test StoryRemoteKeys copy`() {
    val id = "story123"
    val prevKey = 1
    val nextKey = 3

    val storyRemoteKeys = StoryRemoteKeys(id, prevKey, nextKey)
    val copiedStoryRemoteKeys = storyRemoteKeys.copy()

    assertNotNull(copiedStoryRemoteKeys)
    assertEquals(id, copiedStoryRemoteKeys.storyId)
    assertEquals(prevKey, copiedStoryRemoteKeys.prevKey)
    assertEquals(nextKey, copiedStoryRemoteKeys.nextKey)
  }

  @Test
  fun `test StoryRemoteKeys toString`() {
    val id = "story123"
    val prevKey = 1
    val nextKey = 3

    val storyRemoteKeys = StoryRemoteKeys(id, prevKey, nextKey)
    val expectedString = "StoryRemoteKeys(id=story123, prevKey=1, nextKey=3)"

    assertEquals(expectedString, storyRemoteKeys.toString())
  }
}
