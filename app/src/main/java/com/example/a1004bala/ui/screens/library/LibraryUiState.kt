package com.example.a1004bala.ui.screens.library

import com.example.a1004bala.data.model.Book
import com.example.a1004bala.data.model.BookStatus

data class LibraryUiState(
    val selectedStatus: BookStatus = BookStatus.READING,
    val books: List<Book> = emptyList(),
    val counts: Map<BookStatus, Int> = BookStatus.tabOrder.associateWith { 0 }
)