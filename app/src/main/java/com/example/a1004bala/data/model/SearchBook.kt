package com.example.a1004bala.data.model

data class SearchBook(
    val id: String,
    val title: String,
    val author: String,
    val pageCount: Int?,
    val year: String?,
    val coverUrl: String?
)