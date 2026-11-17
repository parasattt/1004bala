package com.example.a1004bala.ui.screens.details

import com.example.a1004bala.data.model.Book
import com.example.a1004bala.data.model.Quote

/** Immutable snapshot of the book details screen. Drafts are separate from saved data. */
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
    val isAddingQuote: Boolean = false,
    val quoteSaveError: String? = null,
    val deletingQuoteIds: Set<Int> = emptySet(),
    val quoteDeleteError: String? = null,
    val isSaving: Boolean = false,
    val actionError: String? = null
)
