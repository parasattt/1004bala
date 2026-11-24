package com.example.a1004bala.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.a1004bala.BookWormApp
import com.example.a1004bala.ui.screens.addbook.AddBookViewModel
import com.example.a1004bala.ui.screens.details.BookDetailsViewModel
import com.example.a1004bala.ui.screens.library.LibraryViewModel
import com.example.a1004bala.ui.screens.search.SearchViewModel
import com.example.a1004bala.ui.screens.search.SettingsViewModel


object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            LibraryViewModel(bookWormApp().container.bookRepository, bookWormApp().container.userPreferences)
        }
        initializer {
            BookDetailsViewModel(
                savedStateHandle = createSavedStateHandle(),
                repository = bookWormApp().container.bookRepository
            )
        }
        initializer {
            AddBookViewModel(createSavedStateHandle(),
                bookWormApp().container.bookRepository, bookWormApp().container.booksRemoteRepository)
        }
        initializer {
            SearchViewModel(bookWormApp().container.booksRemoteRepository)
        }
        initializer {
            SettingsViewModel(bookWormApp().container.userPreferences)
        }
    }
}
fun CreationExtras.bookWormApp(): BookWormApp =
    this[APPLICATION_KEY] as BookWormApp