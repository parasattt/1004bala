package com.example.a1004bala.ui.screens.addbook

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a1004bala.data.model.BookStatus
import com.example.a1004bala.data.repository.BookRepository
import com.example.a1004bala.data.repository.BooksRemoteRepository
import com.example.a1004bala.ui.navigation.Screen
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AddBookViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: BookRepository,
    private val remote: BooksRemoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddBookUiState())
    val uiState: StateFlow<AddBookUiState> = _uiState.asStateFlow()

    init {
        val volumeId = savedStateHandle.get<String>(Screen.AddBook.ARG)
        if (volumeId != null) loadVolume(volumeId)
    }

    private fun loadVolume(volumeId: String) {
        _uiState.update { it.copy(isPrefilling = true, prefillError = null) }
        viewModelScope.launch {
            try {
                val book = remote.getBook(volumeId)
                _uiState.update { it.prefilledFrom(book).copy(isPrefilling = false) }
            } catch (e: IOException) {
                _uiState.update { it.copy(isPrefilling = false, prefillError = ERROR_PREFILL) }
            } catch (e: HttpException) {
                _uiState.update { it.copy(isPrefilling = false, prefillError = ERROR_PREFILL) }
            }
        }
    }



    fun onTitleChange(value: String) {
        _uiState.update {
            it.copy(
                title = value.take(MAX_TITLE_LENGTH),
                errors = it.errors.copy(title = null)
            )
        }
    }

    fun onAuthorChange(value: String) {
        _uiState.update {
            it.copy(
                author = value.take(MAX_AUTHOR_LENGTH),
                errors = it.errors.copy(author = null)
            )
        }
    }

    fun onPagesChange(value: String) {
        val digits = value.filter { it in '0'..'9' }.take(MAX_PAGES_DIGITS)
        _uiState.update {
            it.copy(
                totalPages = digits,
                errors = it.errors.copy(totalPages = null)
            )
        }
    }

    fun onStatusChange(status: BookStatus) {
        _uiState.update { it.copy(status = status) }
    }


    fun validate(): Boolean {
        val state = _uiState.value
        val errors = AddBookErrors(
            title = validateTitle(state.title),
            author = validateAuthor(state.author),
            totalPages = validatePages(state.totalPages)
        )
        _uiState.update { it.copy(errors = errors) }
        return !errors.hasAny
    }


    fun saveBook() {
        if (_uiState.value.isSaving) return
        if (!validate()) return

        val state = _uiState.value
        _uiState.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            repository.addBook(
                title = state.title.trim(),
                author = state.author.trim(),
                totalPages = state.totalPages.trim().toInt(),
                status = state.status,
                coverUrl = state.coverUrl
            )
            _uiState.update { it.copy(isSaving = false, isSaved = true) }
        }
    }
}
