package com.yuoyama12.mywordbook.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface WordbookDao {

    @Transaction
    @Query("SELECT * FROM Wordbook")
    fun loadWordbookAndWords(): Flow<List<WordbookAndWords>>

    @Transaction
    @Query("SELECT * FROM Word WHERE wordbook_id = :wordbookId")
    fun loadWordsBy(wordbookId: Long): Flow<List<Word>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWordbook(wordbook: Wordbook)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: Word)

    @Delete
    suspend fun deleteWordbook(wordbook: Wordbook)

    @Delete
    suspend fun deleteWord(word: Word)

    @Query("UPDATE Wordbook SET modifiedDate = :currentDate WHERE id = :wordbookId")
    suspend fun updateWordbookModifiedDate(wordbookId: Long, currentDate: Date)
}