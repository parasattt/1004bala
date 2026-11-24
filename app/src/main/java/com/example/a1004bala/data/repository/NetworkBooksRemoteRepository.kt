package com.example.a1004bala.data.repository

import com.example.a1004bala.data.model.SearchBook

class NetworkBooksRemoteRepository : BooksRemoteRepository {
    // TODO("Dev 3)
    override suspend fun search(query: String): List<SearchBook> =
        emptyList()

    override suspend fun getBook(volumeId: String): SearchBook =SearchBook(
        id = volumeId,
        title = "",
        author = "",
        pageCount = null,
        year = null,
        coverUrl = null
    )
}