package com.example.collegeschedule.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("favorites")

class FavoritesManager(context: Context) {
    private val dataStore = context.dataStore
    private val FAVORITES_KEY = stringSetPreferencesKey("favorite_groups")
    val favoritesFlow: Flow<Set<String>> = dataStore.data
        .map { preferences -> preferences[FAVORITES_KEY] ?: emptySet() }
    suspend fun addFavorite(groupName: String) {
        dataStore.edit { preferences ->
            val current = preferences[FAVORITES_KEY] ?: emptySet()
            preferences[FAVORITES_KEY] = current + groupName
        }
    }

    suspend fun removeFavorite(groupName: String) {
        dataStore.edit { preferences ->
            val current = preferences[FAVORITES_KEY] ?: emptySet()
            preferences[FAVORITES_KEY] = current - groupName
        }
    }

    suspend fun isFavorite(groupName: String): Boolean {
        val current = dataStore.data.map { it[FAVORITES_KEY] ?: emptySet() }
        return current.first().contains(groupName)
    }

}
