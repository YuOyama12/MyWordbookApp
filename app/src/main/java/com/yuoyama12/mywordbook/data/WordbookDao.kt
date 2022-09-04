package com.yuoyama12.mywordbook.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface WordbookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWordbook(wordbook: Wordbook)
}