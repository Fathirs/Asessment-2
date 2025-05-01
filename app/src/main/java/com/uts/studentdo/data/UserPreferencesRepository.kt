package com.uts.studentdo.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// Extension property for Context to access the DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

    // Keys for our preferences
    companion object {
        val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
        val SORT_ORDER_KEY = stringPreferencesKey("sort_order")
        val PRIORITY_FILTER_KEY = intPreferencesKey("priority_filter")
    }

    // Get the user preferences as a Flow
    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val darkTheme = preferences[DARK_THEME_KEY] ?: false
            val sortOrder = preferences[SORT_ORDER_KEY] ?: "date"
            val priorityFilter = preferences[PRIORITY_FILTER_KEY] ?: 0

            UserPreferences(
                darkTheme = darkTheme,
                sortOrder = sortOrder,
                priorityFilter = priorityFilter
            )
        }

    // Update the dark theme preference
    suspend fun updateDarkTheme(darkTheme: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = darkTheme
        }
    }

    // Update the sort order preference
    suspend fun updateSortOrder(sortOrder: String) {
        context.dataStore.edit { preferences ->
            preferences[SORT_ORDER_KEY] = sortOrder
        }
    }

    // Update the priority filter
    suspend fun updatePriorityFilter(priorityFilter: Int) {
        context.dataStore.edit { preferences ->
            preferences[PRIORITY_FILTER_KEY] = priorityFilter
        }
    }
}

// Data class to hold user preferences
data class UserPreferences(
    val darkTheme: Boolean,
    val sortOrder: String,   // "date", "priority", "name"
    val priorityFilter: Int  // 0: All, 1: Low, 2: Medium, 3: High
)