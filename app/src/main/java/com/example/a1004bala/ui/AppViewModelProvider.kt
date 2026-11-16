package com.example.a1004bala.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.a1004bala.data.repository.BookRepository
import com.example.a1004bala.BookWormApp


class AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            LibraryViewModel(bookWormApp().container.bookRepository)
        }
        initializer {
            BooksDetailsViewModel(
                savedStateHandle = createSavedStateHandle(),
                repository = bookWormApp().container.bookRepository
            )
        }
        initializer {
            AddBookViewModel(bookWormApp().container.bookRepository)
        }
    }
}
fun CreationExtras.bookWormApp(): BookWormApp =
    this[APPLICATION_KEY] as BookWormApp