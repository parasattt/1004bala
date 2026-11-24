package com.example.a1004bala.data

import android.content.Context
import com.example.a1004bala.data.preferences.DataStoreUserPreferencesRepository
import com.example.a1004bala.data.preferences.UserPreferencesRepository
import com.example.a1004bala.data.repository.BookRepository
import com.example.a1004bala.data.repository.BooksRemoteRepository
import com.example.a1004bala.data.repository.InMemoryBookRepository
import com.example.a1004bala.data.repository.NetworkBooksRemoteRepository

class AppContainer(context: Context) {

    val userPreferences: UserPreferencesRepository =
        DataStoreUserPreferencesRepository(context.applicationContext)

    val bookRepository: BookRepository = InMemoryBookRepository()

    val booksRemoteRepository: BooksRemoteRepository = NetworkBooksRemoteRepository()
}