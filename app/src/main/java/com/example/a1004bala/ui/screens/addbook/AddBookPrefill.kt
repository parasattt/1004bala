package com.example.a1004bala.ui.screens.addbook

import com.example.a1004bala.data.model.SearchBook

private const val MAX_PAGES = 99_999

const val ERROR_PREFILL = "Google Books-тан жүктеу мүмкін болмады. Өрістерді өзіңіз толтырыңыз."

fun AddBookUiState.prefilledFrom(book: SearchBook): AddBookUiState {
    val pages = book.pageCount?.takeIf { it in 1..MAX_PAGES }
    return copy(
        title = book.title.take(MAX_TITLE_LENGTH),
        author = book.author.take(MAX_AUTHOR_LENGTH),
        totalPages = pages?.toString().orEmpty(),
        coverUrl = book.coverUrl,
        fromGoogleBooks = true,
        pagesMissing = pages == null,
        errors = AddBookErrors()
    )
}
