package com.ohanyan.mathgame.common.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val MAX_AVAILABLE_NUMBER = intPreferencesKey("max_available_number")
    private val MUSIC_ON = booleanPreferencesKey("music_on")

    val maxAvailableNumber: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[MAX_AVAILABLE_NUMBER] ?: 0
        }

    val musicOn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[MUSIC_ON] ?: true
        }

    suspend fun updateMaxAvailableNumber(number: Int) {
        context.dataStore.edit { preferences ->
            val current = preferences[MAX_AVAILABLE_NUMBER] ?: 0
            if (number > current) {
                preferences[MAX_AVAILABLE_NUMBER] = number
            }
        }
    }

    suspend fun updateMusicOn(isMusicOn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[MUSIC_ON] = isMusicOn
        }
    }
}
