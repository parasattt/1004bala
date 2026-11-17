package com.example.a1004bala.ui.screens.addbook

import com.example.a1004bala.data.model.BookStatus


data class AddBookErrors(
    val title: String? = null,
    val author: String? = null,
    val totalPages: String? = null
) {
    val hasAny: Boolean
        get() = title != null || author != null || totalPages != null
}


data class AddBookUiState(
    val title: String = "",
    val author: String = "",
    val totalPages: String = "",
    val status: BookStatus = BookStatus.PLANNED,
    val errors: AddBookErrors = AddBookErrors(),
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
)
