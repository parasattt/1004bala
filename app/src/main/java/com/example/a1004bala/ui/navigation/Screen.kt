package com.example.a1004bala.ui.navigation

sealed class Screen(val route: String) {
    data object Library : Screen("library")
    data object AddBook : Screen("add_book")
    data object BookDetails : Screen("book_details/{bookId}") {
        const val ARG = "bookId"
        fun createRoute(bookId: Int) = "book_details/$bookId"
    }
}