package com.example.academilink.data.local

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "user_preferences"
)

class UserPreferences(
    private val context: Context
) {
    companion object {
        private val LAST_EMAIL = stringPreferencesKey("last_email")
        private val TOKEN = stringPreferencesKey("token")
    }

    val lastEmail: Flow<String> =
        context.dataStore.data.map { preferences ->
            preferences[LAST_EMAIL] ?: ""
        }

    val token: Flow<String> =
        context.dataStore.data.map { preferences ->
            preferences[TOKEN] ?: ""
        }

    suspend fun saveLastEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[LAST_EMAIL] = email
        }
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }
}