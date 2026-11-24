package com.example.a1004bala.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.a1004bala.data.local.entity.QuoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Query("SELECT * FROM quotes WHERE bookId = :bookId ORDER BY page IS NULL, page ASC, id ASC")
    fun observeQuotes(bookId: Int): Flow<List<QuoteEntity>>

    @Insert
    suspend fun insert(quote: QuoteEntity): Long

    @Query("DELETE FROM quotes WHERE id = :id")
    suspend fun deleteById(id: Int)
}