package dev.erhahahaa.storyapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.erhahahaa.storyapp.data.model.StoryModel
import dev.erhahahaa.storyapp.data.model.StoryRemoteKeys

@Database(entities = [StoryModel::class, StoryRemoteKeys::class], version = 1, exportSchema = false)
abstract class StoryDatabase : RoomDatabase() {
  abstract fun storyDao(): StoryDao

  abstract fun storyRemoteKeysDao(): StoryRemoteKeysDao

  companion object {
    private const val DATABASE_NAME = "story_database"
    private var instance: StoryDatabase? = null

    fun getInstance(context: Context): StoryDatabase {
      if (instance == null) {
        synchronized(StoryDatabase::class) {
          instance =
            Room.databaseBuilder(
                context.applicationContext,
                StoryDatabase::class.java,
                DATABASE_NAME,
              )
              .build()
        }
      }
      return instance!!
    }
  }
}
