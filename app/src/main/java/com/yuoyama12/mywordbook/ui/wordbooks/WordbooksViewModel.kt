package com.yuoyama12.mywordbook.ui.wordbooks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuoyama12.mywordbook.data.Wordbook
import com.yuoyama12.mywordbook.data.WordbookAndWords
import com.yuoyama12.mywordbook.data.WordbookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordbooksViewModel @Inject constructor(
    private val wordbookRepo: WordbookRepository
): ViewModel() {

    private var _wordbookAndWords by mutableStateOf(emptyList<WordbookAndWords>())
    val wordbookAndWords: List<WordbookAndWords>
        get() = _wordbookAndWords

    suspend fun loadWordbookAndWords() {
        wordbookRepo.loadWordbookAndWords().collect {
            _wordbookAndWords = it
        }
    }

    fun addNewWordbook(name: String) {
        val wordbook = Wordbook(name = name)
        viewModelScope.launch(Dispatchers.IO) {
            wordbookRepo.insertWordbook(wordbook)
        }
    }

    fun renameWordbook(wordbook: Wordbook, newName: String) {
        val renamedWordbook = wordbook.copy(name = newName)
        viewModelScope.launch(Dispatchers.IO) {
            wordbookRepo.insertWordbook(renamedWordbook)
        }
    }

    fun deleteWordbook(wordbook: Wordbook) {
        viewModelScope.launch(Dispatchers.IO) {
            wordbookRepo.deleteWordbook(wordbook)
        }
    }

    fun getLatestWordbookBy(wordbookId: Long): Wordbook {
        wordbookAndWords.forEach { wordbookAndWords ->
            if (wordbookAndWords.wordbook.id == wordbookId) {
                return wordbookAndWords.wordbook
            }
        }
        throw Exception("Couldn't find any wordbook from the given workbookId!")
    }

}