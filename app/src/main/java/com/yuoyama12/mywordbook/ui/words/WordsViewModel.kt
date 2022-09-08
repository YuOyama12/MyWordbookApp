package com.yuoyama12.mywordbook.ui.words

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuoyama12.mywordbook.data.Word
import com.yuoyama12.mywordbook.data.WordbookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WordsViewModel @Inject constructor(
    private val wordbookRepo: WordbookRepository
) : ViewModel() {

    private var _words by mutableStateOf(emptyList<Word>())
    val words get() = _words

    val currentDate: Date
        get() {  return Date(System.currentTimeMillis()) }

    fun loadWordsBy(wordbookId: Long) =
        viewModelScope.launch(Dispatchers.IO) {
            wordbookRepo.loadWordsBy(wordbookId).collect {
                _words = it
            }
        }

    fun insertWord(word: Word) {
        viewModelScope.launch(Dispatchers.IO) {
            wordbookRepo.insertWord(word)
            updateWordbookModifiedDate(word.wordbookId)
        }
    }

   private suspend fun updateWordbookModifiedDate(wordbookId: Long) {
       wordbookRepo.updateWordbookModifiedDate(wordbookId, currentDate)
    }
}