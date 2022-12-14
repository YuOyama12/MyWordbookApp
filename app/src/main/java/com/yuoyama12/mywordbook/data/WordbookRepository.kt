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

    fun loadWordsBy(wordbookId: Long): Flow<List<Word>> {
        return wordbookDao.loadWordsBy(wordbookId)
    }

    suspend fun insertWordbook(wordbook: Wordbook) {
        wordbookDao.insertWordbook(wordbook)
    }

    suspend fun insertWord(word: Word) {
        wordbookDao.insertWord(word)
    }

    suspend fun deleteWordbook(wordbook: Wordbook) {
        wordbookDao.deleteWordbook(wordbook)
    }

    suspend fun deleteWord(word: Word) {
        wordbookDao.deleteWord(word)
    }


    suspend fun updateWordbookModifiedDate(wordbookId: Long, currentDate: Date) {
        wordbookDao.updateWordbookModifiedDate(wordbookId, currentDate)
    }

}