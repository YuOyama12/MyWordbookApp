package com.yuoyama12.mywordbook.ui

sealed class Screen(val route: String) {
    object Wordbooks : Screen("wordbooks")
    object Words : Screen("words")
}
