package com.yuoyama12.mywordbook.data

import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class WordbookRepository @Inject constructor(
    private val wordbookDao: WordbookDao
) {
    fun loadWordbookAndWords(): Flow<List<WordbookAndWords>> {
        return wordbookDao.loadWordbookAndWords()
    }

    suspend fun insertWordbook(wordbook: Wordbook) {
        wordbookDao.insertWordbook(wordbook)
    }

    suspend fun insertWord(word: Word) {
        wordbookDao.insertWord(word)
    }

    suspend fun updateWordbookModifiedDate(wordbookId: Long, currentDate: Date) {
        wordbookDao.updateWordbookModifiedDate(wordbookId, currentDate)
    }

    suspend fun deleteWordbook(wordbook: Wordbook) {
        wordbookDao.deleteWordbook(wordbook)
    }
}