package com.yuoyama12.mywordbook.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yuoyama12.mywordbook.NavigationViewModel
import com.yuoyama12.mywordbook.data.Wordbook
import com.yuoyama12.mywordbook.ui.wordbooks.WordbooksScreen
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
        composable(route = Screen.Words.route) {
            val wordbook = navViewModel.wordbook.swapSlashAndRandomString()

            WordsScreen(
                wordbook = wordbook,
                onNavigationIconClicked = { navController.popBackStack()  }
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


