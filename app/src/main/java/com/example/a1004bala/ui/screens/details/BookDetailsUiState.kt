package com.example.a1004bala.ui.screens.details

import com.example.a1004bala.data.model.Book
import com.example.a1004bala.data.model.Quote

data class BookDetailsUiState(
    val book: Book? = null,
    val isLoading: Boolean = true,
    val quotes: List<Quote> = emptyList(),
    val isQuotesLoading: Boolean = true,
    val quotesLoadError: String? = null,
    val pageInput: String = "",
    val pageError: String? = null,
    val quoteInput: String = "",
    val quotePageInput: String = "",
    val quoteError: String? = null,
    val quotePageError: String? = null,
    val isSaving: Boolean = false,
    val actionError: String? = null
)
