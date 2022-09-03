package com.yuoyama12.mywordbook.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yuoyama12.mywordbook.ui.wordbooks.WordbooksScreen

@Composable
fun MyWordbookApp() {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Wordbooks.route
    ) {
        composable(route = Screen.Wordbooks.route) {
            WordbooksScreen()
        }
    }

}

