package com.example.a1004bala.data.repository

import com.example.a1004bala.data.local.dao.BookDao
import com.example.a1004bala.data.local.dao.QuoteDao
import com.example.a1004bala.data.local.entity.BookEntity
import com.example.a1004bala.data.local.entity.QuoteEntity
import com.example.a1004bala.data.local.toBook
import com.example.a1004bala.data.local.toQuote
import com.example.a1004bala.data.model.Book
import com.example.a1004bala.data.model.BookStatus
import com.example.a1004bala.data.model.Quote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomBookRepository(
    private val bookDao: BookDao,
    private val quoteDao: QuoteDao
) : BookRepository {

    override fun getBooks(): Flow<List<Book>> =
        bookDao.observeBooks().map { entities -> entities.map { it.toBook() } }

    override fun getBook(id: Int): Flow<Book?> =
        bookDao.observeBook(id).map { it?.toBook() }

    override fun getQuotes(bookId: Int): Flow<List<Quote>> =
        quoteDao.observeQuotes(bookId).map { entities -> entities.map { it.toQuote() } }


    override suspend fun addBook(
        title: String,
        author: String,
        totalPages: Int,
        status: BookStatus,
        coverUrl: String?
    ): Int {
        val entity = BookEntity(
            title = title.trim(),
            author = author.trim(),
            totalPages = totalPages,
            currentPage = if (status == BookStatus.FINISHED) totalPages else 0,
            status = status,
            coverUrl = coverUrl
        )
        return bookDao.insert(entity).toInt()
    }

    override suspend fun addQuote(bookId: Int, text: String, page: Int?) {
        val clean = text.trim()
        if (clean.isEmpty()) return
        quoteDao.insert(QuoteEntity(bookId = bookId, text = clean, page = page))
    }

    override suspend fun updateBook(book: Book) {
        val old = bookDao.getById(book.id) ?: return
        bookDao.update(
            old.copy(
                title = book.title,
                author = book.author,
                totalPages = book.totalPages,
                currentPage = book.currentPage,
                status = book.status,
                coverUrl = book.coverUrl
            )
        )
    }

    override suspend fun updateCurrentPage(bookId: Int, currentPage: Int) {
        val book = bookDao.getById(bookId) ?: return
        val newPage = currentPage.coerceIn(0, book.totalPages)
        val newStatus = when {
            newPage == book.totalPages -> BookStatus.FINISHED
            newPage > 0 -> BookStatus.READING
            else -> book.status
        }
        bookDao.update(book.copy(currentPage = newPage, status = newStatus))
    }

    override suspend fun updateStatus(bookId: Int, status: BookStatus) {
        val book = bookDao.getById(bookId) ?: return
        val updated = if (status == BookStatus.FINISHED) {
            book.copy(status = status, currentPage = book.totalPages)
        } else {
            book.copy(status = status)
        }
        bookDao.update(updated)
    }

    override suspend fun deleteBook(bookId: Int) {
        bookDao.deleteById(bookId)
    }

    override suspend fun deleteQuote(quoteId: Int) {
        quoteDao.deleteById(quoteId)
    }
}