package com.example.a1004bala.ui.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a1004bala.data.repository.BookRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: BookRepository
) : ViewModel() {
    private val bookId: Int? = savedStateHandle["bookId"]
    private val _uiState = MutableStateFlow(BookDetailsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val id = bookId
        if (id == null) {
            _uiState.update { it.copy(isLoading = false) }
        } else {
            viewModelScope.launch {
                repository.getBook(id)
                    .catch { error ->
                        if (error is CancellationException) throw error
                        _uiState.update { it.copy(isLoading = false, actionError = "Кітапты жүктеу мүмкін болмады") }
                    }
                    .collect { book ->
                        _uiState.update { state ->
                            state.copy(
                                book = book,
                                isLoading = false,
                                pageInput = if (state.book == null) book?.currentPage?.toString().orEmpty() else state.pageInput
                            )
                        }
                    }
            }
        }
    }
}
