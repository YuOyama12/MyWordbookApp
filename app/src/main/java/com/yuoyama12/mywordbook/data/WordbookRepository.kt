package com.yuoyama12.mywordbook.data

import javax.inject.Inject

class WordbookRepository @Inject constructor(
    private val wordbookDao: WordbookDao
) {
    suspend fun insertWordbook(wordbook: Wordbook) {
        wordbookDao.insertWordbook(wordbook)
    }
}