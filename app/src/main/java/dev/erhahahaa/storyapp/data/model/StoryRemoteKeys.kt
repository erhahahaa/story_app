package dev.erhahahaa.storyapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "story_remote_keys")
data class StoryRemoteKeys(
  @PrimaryKey(autoGenerate = false) val storyId: String,
  val prevKey: Int?,
  val nextKey: Int?,
)
