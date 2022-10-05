package com.yuoyama12.mywordbook.ui.words

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuoyama12.mywordbook.WordSorting
import com.yuoyama12.mywordbook.data.Word
import com.yuoyama12.mywordbook.data.Wordbook
import com.yuoyama12.mywordbook.data.WordbookRepository
import com.yuoyama12.mywordbook.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class WordsViewModel @Inject constructor(
    private val wordbookRepo: WordbookRepository,
    dataStoreManager: DataStoreManager
) : ViewModel() {

    val wordSortingFlow = dataStoreManager.getWordSortingFlow()
    val wordSortingOrderFlow = dataStoreManager.getWordSortingOrder()

    private var _words by mutableStateOf(emptyList<Word>())
    val words get() = _words

    private var _isWordsListEmpty by mutableStateOf(words.isEmpty())
    val isWordsListEmpty get() = _isWordsListEmpty

    private var _storedWordText: String? = null
    val storedWordText get () = _storedWordText

    private var _storedMeaningText: String? = null
    val storedMeaningText get() = _storedMeaningText

    val currentDate: Date
        get() {  return Date(System.currentTimeMillis()) }

    fun storeTextsTemporarily(wordText: String, meaningText: String) {
        _storedWordText = wordText
        _storedMeaningText = meaningText
    }

    fun nullifyTemporarilyStoredData() {
        _storedWordText = null
        _storedMeaningText = null
    }

    suspend fun loadWordsBy(wordbookId: Long) {
        wordSortingFlow.flatMapLatest {
            wordbookRepo.loadWordsBy(wordbookId)
        }.collect { list ->
                _words = list.sortBy(
                    wordSortingFlow.first(),
                    wordSortingOrderFlow.first()
                )
                _isWordsListEmpty = list.isEmpty()
        }
    }

    fun insertWord(word: Word) {
        viewModelScope.launch(Dispatchers.IO) {
            wordbookRepo.insertWord(word)
            updateWordbookModifiedDate(word.wordbookId)
        }
    }

    fun deleteWord(word: Word) {
        viewModelScope.launch(Dispatchers.IO) {
            wordbookRepo.deleteWord(word)
            updateWordbookModifiedDate(word.wordbookId)
        }
    }

    fun moveWordToOtherWordbook(word: Word, destination: Wordbook) {
        val wordWithNewWordbookId =
            word.copy(
                wordbookId = destination.id,
                modifiedDate = currentDate
            )

        viewModelScope.launch(Dispatchers.IO) {
            wordbookRepo.deleteWord(word)
            wordbookRepo.insertWord(wordWithNewWordbookId)
            updateWordbookModifiedDate(destination.id)
        }
    }

   private suspend fun updateWordbookModifiedDate(wordbookId: Long) {
       wordbookRepo.updateWordbookModifiedDate(wordbookId, currentDate)
    }
}

private fun List<Word>.sortBy(
    sortName: String,
    isReversed: Boolean
): List<Word> {
    val sortedList = when(WordSorting.valueOf(sortName)) {
        WordSorting.CreatedDate -> this.sortedBy { it.id }
        WordSorting.ModifiedDate -> this.sortedBy { it.modifiedDate }
        WordSorting.Word -> this.sortedBy { it.word }
        WordSorting.Meaning -> this.sortedBy { it.meaning }
    }

    return if (isReversed) sortedList.reversed()
    else  sortedList
}