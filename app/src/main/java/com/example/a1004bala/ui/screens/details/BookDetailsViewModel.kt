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

    fun onPageInputChange(value: String) {
        if (_uiState.value.isSaving) return
        _uiState.update { it.copy(pageInput = value, pageError = null) }
    }

    fun stepPage(delta: Int) {
        val state = _uiState.value
        val book = state.book ?: return
        if (state.isSaving) return
        val page = state.pageInput.trim().toIntOrNull() ?: return
        if (page !in 0..book.totalPages) return
        val nextPage = when {
            delta == 1 && page < book.totalPages -> page + 1
            delta == -1 && page > 0 -> page - 1
            else -> return
        }
        onPageInputChange(nextPage.toString())
    }

    fun updatePages() {
        val state = _uiState.value
        val book = state.book ?: return
        if (state.isSaving) return

        val error = pageInputError(state.pageInput, book.totalPages)
        if (error != null) {
            _uiState.update { it.copy(pageError = error, actionError = null) }
            return
        }
        val page = state.pageInput.trim().toInt()
        _uiState.update { it.copy(isSaving = true, pageError = null, actionError = null) }
        viewModelScope.launch {
            try {
                repository.updateCurrentPage(book.id, page)
                _uiState.update { it.copy(pageInput = page.toString()) }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.update { it.copy(actionError = "Беттерді сақтау мүмкін болмады. Қайта көріңіз.") }
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    fun onQuoteInputChange(value: String) {
        if (_uiState.value.isAddingQuote) return
        _uiState.update { it.copy(quoteInput = value, quoteError = null, quoteSaveError = null) }
    }

    fun onQuotePageInputChange(value: String) {
        if (_uiState.value.isAddingQuote) return
        _uiState.update { it.copy(quotePageInput = value, quotePageError = null, quoteSaveError = null) }
    }

    fun addQuote() {
        val state = _uiState.value
        val book = state.book ?: return
        if (state.isAddingQuote) return
        val textError = quoteTextError(state.quoteInput)
        val pageError = quotePageInputError(state.quotePageInput, book.totalPages)
        if (textError != null || pageError != null) {
            _uiState.update { it.copy(quoteError = textError, quotePageError = pageError, quoteSaveError = null) }
            return
        }
        val text = state.quoteInput.trim()
        val page = state.quotePageInput.trim().toIntOrNull()
        _uiState.update { it.copy(isAddingQuote = true, quoteError = null, quotePageError = null, quoteSaveError = null) }
        viewModelScope.launch {
            try {
                repository.addQuote(book.id, text, page)
                _uiState.update { it.copy(quoteInput = "", quotePageInput = "") }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.update { it.copy(quoteSaveError = "Дәйексөзді сақтау мүмкін болмады. Қайта көріңіз.") }
            } finally {
                _uiState.update { it.copy(isAddingQuote = false) }
            }
        }
    }

    init {
        val id = bookId
        if (id == null) {
            _uiState.update { it.copy(isLoading = false, isQuotesLoading = false) }
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
            viewModelScope.launch {
                repository.getQuotes(id)
                    .catch { error ->
                        if (error is CancellationException) throw error
                        _uiState.update {
                            it.copy(isQuotesLoading = false, quotesLoadError = "Дәйексөздерді жүктеу мүмкін болмады")
                        }
                    }
                    .collect { quotes ->
                        _uiState.update {
                            it.copy(quotes = quotes, isQuotesLoading = false, quotesLoadError = null)
                        }
                    }
            }
        }
    }
}

internal fun pageInputError(value: String, totalPages: Int): String? {
    val page = value.trim().toIntOrNull()
        ?: return "Бүтін сан енгізіңіз"
    return if (page !in 0..totalPages) {
        "0 мен $totalPages аралығындағы санды енгізіңіз"
    } else null
}

internal fun quoteTextError(value: String): String? =
    if (value.isBlank()) "Дәйексөз мәтінін енгізіңіз" else null

internal fun quotePageInputError(value: String, totalPages: Int): String? {
    if (value.isBlank()) return null
    val page = value.trim().toIntOrNull() ?: return "Бүтін сан енгізіңіз"
    return if (page !in 1..totalPages) {
        "1 мен $totalPages аралығындағы санды енгізіңіз"
    } else null
}
