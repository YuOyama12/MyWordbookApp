package com.yuoyama12.mywordbook.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Wordbook::class, Word::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class WordbookDatabase: RoomDatabase() {
    abstract fun wordbookDao(): WordbookDao

    companion object {
        @Volatile
        private var instance: WordbookDatabase? = null

        fun getInstance(context: Context): WordbookDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): WordbookDatabase {
            return Room.databaseBuilder(
                context,
                WordbookDatabase::class.java,
                "wordbook.db"
            ).build()
        }
    }

}