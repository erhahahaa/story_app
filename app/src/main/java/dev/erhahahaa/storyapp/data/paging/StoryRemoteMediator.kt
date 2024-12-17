package dev.erhahahaa.storyapp.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.erhahahaa.storyapp.data.api.ApiService
import dev.erhahahaa.storyapp.data.db.StoryDatabase
import dev.erhahahaa.storyapp.data.model.StoryModel
import dev.erhahahaa.storyapp.data.model.StoryRemoteKeys

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
  private val apiService: ApiService,
  private val token: String,
  private val database: StoryDatabase,
) : RemoteMediator<Int, StoryModel>() {

  override suspend fun load(
    loadType: LoadType,
    state: PagingState<Int, StoryModel>,
  ): MediatorResult {
    val page =
      when (loadType) {
        LoadType.REFRESH -> 1
        LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
        LoadType.APPEND -> {
          val remoteKeys = getRemoteKeyForLastItem(state)
          remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
        }
      }

    try {
      val response = apiService.getStories("Bearer $token", page, state.config.pageSize)
      val stories = response.data ?: emptyList()

      database.withTransaction {
        if (loadType == LoadType.REFRESH) {
          database.storyRemoteKeysDao().clearRemoteKeys()
          database.storyDao().clearStories()
        }

        val keys =
          stories.map { StoryRemoteKeys(storyId = it.id, prevKey = page - 1, nextKey = page + 1) }
        database.storyRemoteKeysDao().insertAll(keys)
        database.storyDao().insertAll(stories)
      }

      return MediatorResult.Success(endOfPaginationReached = stories.isEmpty())
    } catch (e: Exception) {
      return MediatorResult.Error(e)
    }
  }

  private suspend fun getRemoteKeyForLastItem(
    state: PagingState<Int, StoryModel>
  ): StoryRemoteKeys? {
    return state.pages
      .lastOrNull { it.data.isNotEmpty() }
      ?.data
      ?.lastOrNull()
      ?.let { story -> database.storyRemoteKeysDao().remoteKeysStoryId(story.id) }
  }
}
