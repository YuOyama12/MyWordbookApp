package com.yuoyama12.mywordbook.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WordbookDao {

    @Transaction
    @Query("SELECT * FROM Wordbook")
    fun loadWordbookAndWords(): Flow<List<WordbookAndWords>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWordbook(wordbook: Wordbook)

    @Delete
    suspend fun deleteWordbook(wordbook: Wordbook)
}