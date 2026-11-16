package com.example.a1004bala.ui.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.a1004bala.data.repository.BookRepository

class BookDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: BookRepository
) : ViewModel() {
    // TODO:  bookId получить с savedStateHandle uiState, updatePages(), addQuote(), deleteQuote(), changeStatus()
}