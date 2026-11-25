package com.example.a1004bala.data.repository

import com.example.a1004bala.data.model.SearchBook
import com.example.a1004bala.data.remote.GoogleBooksApi
import com.example.a1004bala.data.remote.RetrofitClient
import com.example.a1004bala.data.remote.dto.toSearchBook

class NetworkBooksRemoteRepository(
    private val api: GoogleBooksApi = RetrofitClient.api,
    // Dev 1 can supply the team's configured key through AppContainer.
    private val apiKey: String? = null
) : BooksRemoteRepository {
    override suspend fun search(query: String): List<SearchBook> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return emptyList()
        return api.searchBooks(trimmed, apiKey = apiKey?.trim()?.takeIf { it.isNotEmpty() }).items.orEmpty()
            .filter { !it.id.isNullOrBlank() }
            .map { it.toSearchBook() }
            .distinctBy { it.id }
    }

    override suspend fun getBook(volumeId: String): SearchBook {
        require(volumeId.isNotBlank()) { "volumeId must not be blank" }
        return api.getVolume(volumeId.trim(), apiKey = apiKey?.trim()?.takeIf { it.isNotEmpty() }).toSearchBook()
    }
}
