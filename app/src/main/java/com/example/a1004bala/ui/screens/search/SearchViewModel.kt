package com.example.a1004bala.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a1004bala.data.repository.BooksRemoteRepository
import com.google.gson.JsonParseException
import java.io.IOException
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SearchViewModel(private val repository: BooksRemoteRepository) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()
    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState = _uiState.asStateFlow()
    private var searchJob: Job? = null
    private var lastRequestedQuery: String? = null

    fun onQueryChange(value: String) {
        if (value == _query.value) return
        searchJob?.cancel()
        _query.value = value
        _uiState.value = SearchUiState.Idle
    }

    fun clear() = onQueryChange("")

    fun retry() {
        if (_uiState.value !is SearchUiState.Error) return
        _query.value = lastRequestedQuery ?: return
        search()
    }

    fun search() {
        searchJob?.cancel()
        val requested = _query.value.trim()
        if (requested.isBlank()) {
            _uiState.value = SearchUiState.Idle
            return
        }
        _uiState.value = SearchUiState.Loading
        lastRequestedQuery = requested
        searchJob = viewModelScope.launch {
            val result = try {
                val books = repository.search(requested)
                if (books.isEmpty()) SearchUiState.Empty(requested) else SearchUiState.Success(books)
            } catch (e: IOException) {
                SearchUiState.Error("Интернет байланысын тексеріңіз де, қайта көріңіз.")
            } catch (e: HttpException) {
                SearchUiState.Error(if (e.code() == 429) "Сұрау саны шектелді. Сәлден кейін қайталаңыз."
                    else "Сервер қатесі (${e.code()}). Қайта көріңіз.")
            } catch (e: JsonParseException) {
                SearchUiState.Error("Сервер жауабын оқу мүмкін болмады. Қайта көріңіз.")
            }
            // Even a repository that ignores cancellation cannot overwrite a newer query.
            ensureActive()
            _uiState.value = result
        }
    }
}
