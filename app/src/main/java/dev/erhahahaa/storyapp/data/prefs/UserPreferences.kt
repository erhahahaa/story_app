package dev.erhahahaa.storyapp.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(context: Context) {

  companion object {
    private const val PREF_NAME = "user_prefs"
    private val TOKEN_KEY = stringPreferencesKey("token")

    @Volatile private var INSTANCE: UserPreferences? = null

    fun getInstance(context: Context): UserPreferences {
      return INSTANCE
        ?: synchronized(this) { INSTANCE ?: UserPreferences(context).also { INSTANCE = it } }
    }
  }

  private val Context.dataStore by preferencesDataStore(name = PREF_NAME)

  private val dataStore = context.dataStore

  suspend fun saveSession(token: String) {
    dataStore.edit { preferences -> preferences[TOKEN_KEY] = token }
  }

  suspend fun getToken(): String? {
    return dataStore.data.map { preferences -> preferences[TOKEN_KEY] }.firstOrNull()
  }

  suspend fun clearSession() {
    dataStore.edit { preferences -> preferences.remove(TOKEN_KEY) }
  }
}
