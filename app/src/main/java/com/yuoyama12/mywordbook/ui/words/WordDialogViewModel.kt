package com.yuoyama12.mywordbook.ui.words

import androidx.lifecycle.ViewModel

class WordDialogViewModel : ViewModel() {
    private var _preInputtedWord = ""
    val preInputtedWord get() = _preInputtedWord

    private var _inputtedWord = ""
    val inputtedWord get() = _inputtedWord

    private var _preInputtedMeaning = ""
    val preInputtedMeaning get() = _preInputtedMeaning

    private var _inputtedMeaning = ""
    val inputtedMeaning get() = _inputtedMeaning

    fun setPreInputtedWord(preInputtedWord: String) {
        _preInputtedWord = preInputtedWord
    }

    fun setPreInputtedMeaning(preInputtedMeaning: String) {
        _preInputtedMeaning = preInputtedMeaning
    }

    fun setInputtedWord(inputtedWord: String) {
        _inputtedWord = inputtedWord
    }

    fun setInputtedMeaning(inputtedMeaning: String) {
        _inputtedMeaning = inputtedMeaning
    }
}