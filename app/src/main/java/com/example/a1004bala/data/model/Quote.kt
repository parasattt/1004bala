package com.example.a1004bala.data.model

data class Quote(
    val id: Int,
    val bookId: Int,
    val text: String,
    val page: Int? = null
)