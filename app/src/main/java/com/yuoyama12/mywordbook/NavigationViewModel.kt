package com.yuoyama12.mywordbook

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.yuoyama12.mywordbook.data.Wordbook

class NavigationViewModel: ViewModel() {
    private var _wordbook by mutableStateOf(Wordbook(id = -1, name = ""))
    val wordbook get() = _wordbook

    fun setWordbook(selectedWordbook: Wordbook) {
        _wordbook = selectedWordbook
    }
}