package com.example.a1004bala.ui.navigation

sealed class Screen(val route: String) {
    data object Library : Screen("library")
    data object Search : Screen("search") //TODO screens is not done
    data object Settings : Screen("settings")//TODO screens is not done


    data object AddBook : Screen("add_book?volumeId={volumeId}") {
        const val ARG = "volumeId"
        fun createRoute(volumeId: String? = null): String =
            if (volumeId == null) "add_book" else "add_book?volumeId=$volumeId"
    }

    data object BookDetails : Screen("book_details/{bookId}") {
        const val ARG = "bookId"
        fun createRoute(bookId: Int) = "book_details/$bookId"
    }
}