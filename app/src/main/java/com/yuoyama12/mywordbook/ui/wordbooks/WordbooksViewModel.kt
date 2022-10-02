package com.yuoyama12.mywordbook.ui.wordbooks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuoyama12.mywordbook.Sorting
import com.yuoyama12.mywordbook.data.Wordbook
import com.yuoyama12.mywordbook.data.WordbookAndWords
import com.yuoyama12.mywordbook.data.WordbookRepository
import com.yuoyama12.mywordbook.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class WordbooksViewModel @Inject constructor(
    private val wordbookRepo: WordbookRepository,
    dataStoreManager: DataStoreManager
): ViewModel() {

    val wordbookSortingFlow = dataStoreManager.getWordbookSortingFlow()
    val wordbookSortingOrderFlow = dataStoreManager.getWordbookSortingOrder()

    private var _wordbookAndWordsList by mutableStateOf(emptyList<WordbookAndWords>())
    val wordbookAndWordsList get() = _wordbookAndWordsList

    private var _isWordbookEmpty by mutableStateOf(wordbookAndWordsList.isEmpty())
    val isWordbookEmpty get() = _isWordbookEmpty

    suspend fun loadWordbookAndWords() {
        wordbookSortingFlow.flatMapLatest {
            wordbookRepo.loadWordbookAndWords()
        }.collect { list ->
            _wordbookAndWordsList = list.sortBy(
                wordbookSortingFlow.first(),
                wordbookSortingOrderFlow.first()
            )
            _isWordbookEmpty = list.isEmpty()
        }
    }

    fun fetchWordbooksAndWords(): List<WordbookAndWords> {
        return runBlocking {
            wordbookRepo.loadWordbookAndWords().first()
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
        wordbookAndWordsList.forEach { wordbookAndWords ->
            if (wordbookAndWords.wordbook.id == wordbookId) {
                return wordbookAndWords.wordbook
            }
        }
        throw Exception("Couldn't find any wordbook from the given workbookId!")
    }

}

private fun List<WordbookAndWords>.sortBy(
    sortName: String,
    isReversed: Boolean
): List<WordbookAndWords> {
    val sortedList = when(Sorting.valueOf(sortName)) {
        Sorting.CreatedDate -> this.sortedBy { it.wordbook.id }
        Sorting.ModifiedDate -> this.sortedBy { it.wordbook.modifiedDate }
        Sorting.Name -> this.sortedBy { it.wordbook.name }
    }

    return if (isReversed) sortedList.reversed()
    else  sortedList
}