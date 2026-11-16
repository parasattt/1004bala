package com.example.a1004bala.data.repository

import com.example.a1004bala.data.model.Book
import com.example.a1004bala.data.model.BookStatus
import com.example.a1004bala.data.model.Quote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.concurrent.atomic.AtomicInteger

class InMemoryBookRepository: BookRepository{
    private val books = MutableStateFlow(SampleData.books)
    private val quotes = MutableStateFlow(SampleData.quotes)

    private val nextBookId = AtomicInteger((SampleData.books.maxOfOrNull { it.id } ?: 0) + 1)
    private val nextQuoteId = AtomicInteger((SampleData.quotes.maxOfOrNull { it.id } ?: 0) + 1)

    override fun getBooks(): Flow<List<Book>> = books

    override fun getBook(id: Int): Flow<Book?> =
        books.map { list -> list.find { it.id == id } }

    override fun getQuotes(bookId: Int): Flow<List<Quote>> =
        quotes.map { list ->
            list.filter { it.bookId == bookId }
                .sortedWith(compareBy(nullsLast()) { it.page })
        }

    override suspend fun addBook(
        title: String,
        author: String,
        totalPages: Int,
        status: BookStatus
    ): Int {
        val id = nextBookId.getAndIncrement()
        val book = Book(
            id = id,
            title = title.trim(),
            author = author.trim(),
            totalPages = totalPages,
            currentPage = if (status == BookStatus.FINISHED) totalPages else 0,
            status = status
        )
        books.update { it + book }
        return id
    }

    override suspend fun updateBook(book: Book) {
        books.update { list -> list.map { if (it.id == book.id) book else it } }
    }

    override suspend fun updateCurrentPage(bookId: Int, page: Int) {
        updateWhere(bookId) { book ->
            val newPage = page.coerceIn(0, book.totalPages)
            val newStatus = when {
                newPage == book.totalPages -> BookStatus.FINISHED
                newPage > 0 -> BookStatus.READING
                else -> book.status
            }
            book.copy(currentPage = newPage, status = newStatus)
        }
    }

    override suspend fun updateStatus(bookId: Int, status: BookStatus) {
        updateWhere(bookId) { book ->
            when (status) {
                BookStatus.FINISHED -> book.copy(status = status, currentPage = book.totalPages)
                else -> book.copy(status = status)
            }
        }
    }

    override suspend fun deleteBook(bookId: Int) {
        books.update { list -> list.filterNot { it.id == bookId } }
        quotes.update { list -> list.filterNot { it.bookId == bookId } }
    }

    override suspend fun addQuote(bookId: Int, text: String, page: Int?) {
        val clean = text.trim()
        if (clean.isEmpty()) return
        val quote = Quote(
            id = nextQuoteId.getAndIncrement(),
            bookId = bookId,
            text = clean,
            page = page
        )
        quotes.update { it + quote }
    }

    override suspend fun deleteQuote(quoteId: Int) {
        quotes.update { list -> list.filterNot { it.id == quoteId } }
    }


    private inline fun updateWhere(bookId: Int, crossinline transform: (Book) -> Book) {
        books.update { list -> list.map { if (it.id == bookId) transform(it) else it } }
    }
}

