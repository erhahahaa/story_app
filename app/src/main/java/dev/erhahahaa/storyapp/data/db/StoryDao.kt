package dev.erhahahaa.storyapp.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.erhahahaa.storyapp.data.model.StoryModel

@Dao
interface StoryDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(stories: List<StoryModel>)

  @Query("SELECT * FROM stories") fun getAllStories(): PagingSource<Int, StoryModel>

  @Query("DELETE FROM stories") suspend fun clearStories()
}
