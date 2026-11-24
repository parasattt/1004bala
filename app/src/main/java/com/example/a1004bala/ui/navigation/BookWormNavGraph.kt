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
import com.example.a1004bala.ui.screens.search.SearchScreen
import com.example.a1004bala.ui.screens.settings.SettingsScreen

@Composable
fun BookWormNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val goToLibrary: () -> Unit = {
        navController.popBackStack(Screen.Library.route, inclusive = false)
    }
    val openSearch: () -> Unit = {
        navController.navigate(Screen.Search.route) { launchSingleTop = true }
    }

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
                    navController.navigate(Screen.AddBook.createRoute()) { launchSingleTop = true }
                },
                onSearchClick = openSearch,
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route) { launchSingleTop = true }
                }
            )
        }

        composable(
            route = Screen.AddBook.route,
            arguments = listOf(navArgument(Screen.AddBook.ARG) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) {
            AddBookScreen(
                onBack = { navController.popBackStack() },
                onLibraryClick = goToLibrary,
                onSearchClick = openSearch
            )
        }

        composable(
            route = Screen.BookDetails.route,
            arguments = listOf(navArgument(Screen.BookDetails.ARG) { type = NavType.IntType })
        ) {
            BookDetailsScreen(
                onBack = { navController.popBackStack() },
                onLibraryClick = goToLibrary
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onBack = { navController.popBackStack() },
                onResultClick = { volumeId ->
                    navController.navigate(Screen.AddBook.createRoute(volumeId))
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}