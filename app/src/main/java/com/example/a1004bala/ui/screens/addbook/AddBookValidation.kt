package com.example.a1004bala.ui.screens.addbook


const val MAX_TITLE_LENGTH = 100
const val MAX_AUTHOR_LENGTH = 80
const val MAX_PAGES_DIGITS = 5

const val ERROR_TITLE = "Кітап атауын енгізіңіз"
const val ERROR_AUTHOR = "Автор атын енгізіңіз"
const val ERROR_PAGES = "Беттер саны 0-ден көп болуы керек"

fun validateTitle(title: String): String? =
    if (title.isBlank()) ERROR_TITLE else null

fun validateAuthor(author: String): String? =
    if (author.isBlank()) ERROR_AUTHOR else null


fun validatePages(pages: String): String? {
    val number = pages.trim().toIntOrNull()
    return if (number == null || number <= 0) ERROR_PAGES else null
}
