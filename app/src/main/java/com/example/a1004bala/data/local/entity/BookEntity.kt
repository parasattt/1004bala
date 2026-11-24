package com.example.a1004bala.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.a1004bala.data.model.BookStatus

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val author: String,
    val totalPages: Int,
    val currentPage: Int = 0,
    val status: BookStatus,
    val coverUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)