package com.example.a1004bala.data

import com.example.a1004bala.data.repository.BookRepository
import com.example.a1004bala.data.repository.InMemoryBookRepository

class AppContainer {
    val bookRepository: BookRepository = InMemoryBookRepository()
}