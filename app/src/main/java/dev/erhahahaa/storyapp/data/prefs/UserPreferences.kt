package dev.erhahahaa.storyapp.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.erhahahaa.storyapp.data.model.User
import dev.erhahahaa.storyapp.utils.extensions.toUser
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(context: Context) {

  companion object {
    private const val PREF_NAME = "user_prefs"
    private val USER_KEY = stringPreferencesKey("user")

    @Volatile private var INSTANCE: UserPreferences? = null

    fun getInstance(context: Context): UserPreferences {
      return INSTANCE
        ?: synchronized(this) { INSTANCE ?: UserPreferences(context).also { INSTANCE = it } }
    }
  }

  private val Context.dataStore by preferencesDataStore(name = PREF_NAME)

  private val dataStore = context.dataStore

  suspend fun saveUser(user: User) {
    dataStore.edit { preferences -> preferences[USER_KEY] = user.toString() }
  }

  suspend fun getUser(): User? {
    val res = dataStore.data.map { preferences -> preferences[USER_KEY] }.firstOrNull()
    return res?.toUser()
  }

  suspend fun clearUser() {
    dataStore.edit { preferences -> preferences.remove(USER_KEY) }
  }
}
