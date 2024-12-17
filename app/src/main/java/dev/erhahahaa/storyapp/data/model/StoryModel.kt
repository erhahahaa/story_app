package dev.erhahahaa.storyapp.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@Entity(tableName = "stories")
data class StoryModel(
  @PrimaryKey(autoGenerate = false) val id: String,
  val name: String,
  val description: String,
  val photoUrl: String,
  val lat: Double?,
  val lon: Double?,
  val createdAt: String,
) : Parcelable
