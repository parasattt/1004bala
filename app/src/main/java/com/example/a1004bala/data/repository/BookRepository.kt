package com.example.a1004bala.data.repository

import com.example.a1004bala.data.model.Book
import com.example.a1004bala.data.model.BookStatus
import com.example.a1004bala.data.model.Quote
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getBooks(): Flow<List<Book>>
    fun getBook(id: Int): Flow<Book>
    fun getQuotes(bookId: Int): Flow<List<Quote>>

    suspend fun addBook(title: String, author: String,
                        totalPages: Int, status: BookStatus): Int
    suspend fun updateBook(book: Book)
    suspend fun updateCurrentPage(bookId: Int, currentPage: Int)
    suspend fun updateStatus(bookId: Int, status: BookStatus)
    suspend fun deleteBook(bookId: Int)
    suspend fun addQuote(bookId: Int, text: String, page: Int?)
    suspend fun deleteQuote(quoteId: Int)
}