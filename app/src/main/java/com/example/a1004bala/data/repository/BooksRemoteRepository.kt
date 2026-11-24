package com.example.a1004bala.data.repository

import com.example.a1004bala.data.model.SearchBook

interface BooksRemoteRepository {
    suspend fun search(query: String): List<SearchBook>
    suspend fun getBook(volumeId: String): SearchBook
}