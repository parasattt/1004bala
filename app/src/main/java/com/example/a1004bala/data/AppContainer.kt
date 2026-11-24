package com.example.a1004bala.data

import android.content.Context
import com.example.a1004bala.data.local.BookWormDatabase
import com.example.a1004bala.data.preferences.DataStoreUserPreferencesRepository
import com.example.a1004bala.data.preferences.UserPreferencesRepository
import com.example.a1004bala.data.repository.BookRepository
import com.example.a1004bala.data.repository.BooksRemoteRepository
import com.example.a1004bala.data.repository.NetworkBooksRemoteRepository
import com.example.a1004bala.data.repository.RoomBookRepository

class AppContainer(context: Context) {

    private val database = BookWormDatabase.getInstance(context)
    val userPreferences: UserPreferencesRepository =
        DataStoreUserPreferencesRepository(context.applicationContext)
    val bookRepository: BookRepository =
        RoomBookRepository(database.bookDao(), database.quoteDao())
    val booksRemoteRepository: BooksRemoteRepository = NetworkBooksRemoteRepository()
}