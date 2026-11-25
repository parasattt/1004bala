package com.example.a1004bala.ui.screens.search

import com.example.a1004bala.data.model.SearchBook

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Loading : SearchUiState
    data class Success(val books: List<SearchBook>) : SearchUiState
    data class Empty(val query: String) : SearchUiState
    data class Error(val message: String, val title: String = "Іздеу орындалмады") : SearchUiState
}
