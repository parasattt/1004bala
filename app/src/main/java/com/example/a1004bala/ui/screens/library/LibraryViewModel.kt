package com.example.a1004bala.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a1004bala.data.model.BookStatus
import com.example.a1004bala.data.preferences.SortOrder
import com.example.a1004bala.data.preferences.UserPreferencesRepository
import com.example.a1004bala.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val repository: BookRepository,
    private val preferences: UserPreferencesRepository
) : ViewModel() {
    private val selectedStatus = MutableStateFlow(BookStatus.READING)

    val uiState: StateFlow<LibraryUiState> = combine(
        repository.getBooks(),
        selectedStatus,
        preferences.sortOrder
    ) { books, status, sortOrder ->
        val visibleBooks = books.filter { it.status == status }
        val sortedBooks = when (sortOrder) {
            SortOrder.RECENT -> visibleBooks
            SortOrder.TITLE -> visibleBooks.sortedBy { it.title.lowercase() }
            SortOrder.PROGRESS -> visibleBooks.sortedByDescending { it.progress }
        }

        LibraryUiState(
            selectedStatus = status,
            books = sortedBooks,
            sortOrder = sortOrder,
            counts = BookStatus.tabOrder.associateWith { tabStatus ->
                books.count { it.status == tabStatus }
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LibraryUiState()
    )

    fun onTabSelected(status: BookStatus) {
        selectedStatus.value = status
    }

    fun onSortSelected(order: SortOrder) {
        viewModelScope.launch {
            preferences.setSortOrder(order)
        }
    }

    fun deleteBook(bookId: Int) {
        viewModelScope.launch {
            repository.deleteBook(bookId)
        }
    }
}
