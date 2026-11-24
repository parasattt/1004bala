package com.example.a1004bala.ui.screens.library

import androidx.lifecycle.ViewModel
import com.example.a1004bala.data.repository.BookRepository
import androidx.lifecycle.viewModelScope
import com.example.a1004bala.data.model.BookStatus
import com.example.a1004bala.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val repository: BookRepository,
    private val preferences: UserPreferencesRepository
): ViewModel(){
    private val selectedStatus = MutableStateFlow(BookStatus.READING)
    val uiState: StateFlow<LibraryUiState> = combine(
        repository.getBooks(),
        selectedStatus

    ) { books, status ->
        LibraryUiState(
            selectedStatus = status,
            books = books.filter { it.status == status },
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

    fun deleteBook(bookId: Int) {
        viewModelScope.launch {
            repository.deleteBook(bookId)
        }
    }
}