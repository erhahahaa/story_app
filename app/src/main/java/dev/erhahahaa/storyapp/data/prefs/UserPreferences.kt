package dev.erhahahaa.storyapp.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class UserPreferences(private val context: Context) {

  companion object {
    private const val PREF_NAME = "user_prefs"
    private val TOKEN_KEY = stringPreferencesKey("token")
  }

  private val Context.dataStore by preferencesDataStore(name = PREF_NAME)

  suspend fun saveSession(token: String) {
    context.dataStore.edit { preferences -> preferences[TOKEN_KEY] = token }
  }

  fun getToken() = context.dataStore.data.map { preferences -> preferences[TOKEN_KEY] }

  suspend fun clearSession() {
    context.dataStore.edit { preferences -> preferences.remove(TOKEN_KEY) }
  }
}
