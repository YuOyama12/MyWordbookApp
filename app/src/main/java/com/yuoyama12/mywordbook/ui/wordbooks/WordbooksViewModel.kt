package com.yuoyama12.mywordbook.ui.wordbooks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuoyama12.mywordbook.data.Wordbook
import com.yuoyama12.mywordbook.data.WordbookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordbooksViewModel @Inject constructor(
    private val wordbookRepo: WordbookRepository
): ViewModel() {

    fun addNewWordbook(name: String) {
        val wordbook = Wordbook(name = name)
        viewModelScope.launch(Dispatchers.IO) {
            wordbookRepo.insertWordbook(wordbook)
        }
    }

}