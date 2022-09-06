package com.yuoyama12.mywordbook.ui

import com.yuoyama12.mywordbook.data.Wordbook

sealed class Screen(val route: String) {
    object Wordbooks : Screen("wordbooks")
    object Words : Screen("words/{wordbook}") {
        fun createRoute(wordbook: Wordbook) =
            "words/$wordbook"
    }
}
