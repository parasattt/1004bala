package com.example.a1004bala.ui.screens.library

import com.example.a1004bala.data.model.Book
import com.example.a1004bala.data.model.BookStatus
import com.example.a1004bala.data.preferences.SortOrder

data class LibraryUiState(
    val selectedStatus: BookStatus = BookStatus.READING,
    val books: List<Book> = emptyList(),
    val counts: Map<BookStatus, Int> = BookStatus.tabOrder.associateWith { 0 },
    val sortOrder: SortOrder = SortOrder.RECENT,
    val isLoading: Boolean = true
)
