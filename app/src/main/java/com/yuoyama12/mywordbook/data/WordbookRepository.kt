package com.yuoyama12.mywordbook.data

import kotlinx.coroutines.flow.Flow
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
}