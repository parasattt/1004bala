package com.example.a1004bala.data.local

import com.example.a1004bala.data.local.entity.BookEntity
import com.example.a1004bala.data.local.entity.QuoteEntity
import com.example.a1004bala.data.model.Book
import com.example.a1004bala.data.model.Quote


fun BookEntity.toBook(): Book = Book(
    id = id,
    title = title,
    author = author,
    totalPages = totalPages,
    currentPage = currentPage,
    status = status,
    coverUrl = coverUrl
)

fun QuoteEntity.toQuote(): Quote = Quote(
    id = id,
    bookId = bookId,
    text = text,
    page = page
)