package com.example.a1004bala.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.a1004bala.ui.screens.addbook.AddBookScreen
import com.example.a1004bala.ui.screens.details.BookDetailsScreen
import com.example.a1004bala.ui.screens.library.LibraryScreen

@Composable
fun BookWormNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Library.route,
        modifier = modifier
    ) {
        composable(Screen.Library.route) {
            LibraryScreen(
                onBookClick = { id ->
                    navController.navigate(Screen.BookDetails.createRoute(id))
                },
                onAddClick = {
                    navController.navigate(Screen.AddBook.route) { launchSingleTop = true }
                }
            )
        }

        composable(Screen.AddBook.route) {
            AddBookScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.BookDetails.route,
            arguments = listOf(navArgument(Screen.BookDetails.ARG) { type = NavType.IntType })
        ) {
            BookDetailsScreen(onBack = { navController.popBackStack() })
        }
    }
}