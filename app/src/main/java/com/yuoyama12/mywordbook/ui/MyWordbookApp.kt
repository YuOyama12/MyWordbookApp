package com.yuoyama12.mywordbook.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yuoyama12.mywordbook.NavigationViewModel
import com.yuoyama12.mywordbook.data.Word
import com.yuoyama12.mywordbook.data.Wordbook
import com.yuoyama12.mywordbook.ui.wordbooks.WordbooksScreen
import com.yuoyama12.mywordbook.ui.worddetail.WordDetailScreen
import com.yuoyama12.mywordbook.ui.words.WordsScreen

//文字列に'/'が含まれていた場合、画面遷移に失敗するため
//適当な文字列に置き換えることとする。
private const val RANDOM_STRING = "od8bwx1MQ"

@Composable
fun MyWordbookApp() {

    val navViewModel: NavigationViewModel = hiltViewModel()
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Wordbooks.route
    ) {
        composable(route = Screen.Wordbooks.route) { backStackEntry ->
            WordbooksScreen(
                onWordbookClicked = { _wordbook ->
                    if (backStackEntry.lifecycleIsResumed()) {
                        val wordbook = _wordbook.swapSlashAndRandomString()
                        navViewModel.setWordbook(wordbook)

                        navController.navigate(Screen.Words.createRoute(wordbook))
                    }
                }
            )
        }
        composable(route = Screen.Words.route) { backStackEntry ->
            val wordbook = navViewModel.wordbook.swapSlashAndRandomString()

            WordsScreen(
                wordbook = wordbook,
                onWordDetailMenuClicked = { _word ->
                    if (backStackEntry.lifecycleIsResumed()) {
                        val word = _word.swapSlashAndRandomString()
                        navViewModel.setWord(word)

                        navController.navigate(Screen.WordDetail.createRoute(word))
                    }

                },
                onNavigationIconClicked = { navController.popBackStack()  },
            )
        }
        composable(route = Screen.WordDetail.route) {
            val word = navViewModel.word.swapSlashAndRandomString()

            WordDetailScreen(
                word = word,
                onNavigationIconClicked = { navController.popBackStack() }
            )
        }
    }

}

private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

private fun Wordbook.swapSlashAndRandomString(): Wordbook {
    if (!this.toString().contains("/") &&
        !this.toString().contains(RANDOM_STRING)
    ) {
        return this
    }

    return if (this.toString().contains("/")) {
        val newWordbookName = this.name.replace("/", RANDOM_STRING)

        this.copy(name = newWordbookName)
    } else {
        val newWordbookName = this.name.replace(RANDOM_STRING, "/")

        this.copy(name = newWordbookName)
    }
}

private fun Word.swapSlashAndRandomString(): Word {
    if (!this.toString().contains("/") &&
        !this.toString().contains(RANDOM_STRING)
    ) {
        return this
    }

    return if (this.toString().contains("/")) {
        val newWordText = this.word.replace("/", RANDOM_STRING)
        val newMeaningText = this.meaning.replace("/", RANDOM_STRING)
        val newMiscellaneousNoteText = this.miscellaneousNote.replace("/", RANDOM_STRING)

        this.copy(word = newWordText, meaning = newMeaningText, miscellaneousNote = newMiscellaneousNoteText)
    } else {
        val newWordText = this.word.replace(RANDOM_STRING, "/")
        val newMeaningText = this.meaning.replace(RANDOM_STRING, "/")
        val newMiscellaneousNoteText = this.miscellaneousNote.replace(RANDOM_STRING, "/")

        this.copy(word = newWordText, meaning = newMeaningText, miscellaneousNote = newMiscellaneousNoteText)
    }
}


