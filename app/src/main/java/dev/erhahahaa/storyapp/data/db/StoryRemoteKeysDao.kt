package dev.erhahahaa.storyapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.erhahahaa.storyapp.data.model.StoryRemoteKeys

@Dao
interface StoryRemoteKeysDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(remoteKeys: List<StoryRemoteKeys>)

  @Query("SELECT * FROM story_remote_keys WHERE storyId = :storyId")
  suspend fun remoteKeysStoryId(storyId: String): StoryRemoteKeys?

  @Query("DELETE FROM story_remote_keys") suspend fun clearRemoteKeys()
}
