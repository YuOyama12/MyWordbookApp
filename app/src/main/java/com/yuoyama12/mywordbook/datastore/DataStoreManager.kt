package com.yuoyama12.mywordbook.datastore

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "datastore"
private val defaultWordTagsList = listOf("[名]", "[動]", "[形]", "[副]", "[前]", "[助動]")

class DataStoreManager(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATASTORE_NAME)
        val SHOW_MEANING = booleanPreferencesKey("showMeaning")
        val WORD_TAGS = stringPreferencesKey("wordTags")
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

    suspend fun getWordTags(): SnapshotStateList<String> {
        val listType = object : TypeToken<SnapshotStateList<String>>(){}.type

        val wordTagsStr =
            context.dataStore.data.map { preferences ->
                preferences[WORD_TAGS] ?: defaultWordTagsList.toJson()
            }.first()

        return Gson().fromJson(wordTagsStr, listType)
    }

    suspend fun storeWordTags(tagsList: List<String>) {
        val wordTagsStr = tagsList.toJson()

        context.dataStore.edit { mutablePreferences ->
            mutablePreferences[WORD_TAGS] = wordTagsStr
        }
    }

}

private fun Any.toJson(): String = Gson().toJson(this)