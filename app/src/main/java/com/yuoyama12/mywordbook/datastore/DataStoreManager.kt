package com.yuoyama12.mywordbook.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "datastore"
class DataStoreManager(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATASTORE_NAME)
        val SHOW_MEANING = booleanPreferencesKey("showMeaning")
    }

    fun getWhetherShowMeaning(): Flow<Boolean> =
        context.dataStore.data.map { preferences ->
            preferences[SHOW_MEANING] ?: true
        }

    suspend fun storeWhetherShowMeaning(bool: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SHOW_MEANING] = bool
        }
    }

}