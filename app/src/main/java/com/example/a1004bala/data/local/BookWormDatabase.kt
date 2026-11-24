package com.example.a1004bala.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.a1004bala.data.local.dao.BookDao
import com.example.a1004bala.data.local.dao.QuoteDao
import com.example.a1004bala.data.local.entity.BookEntity
import com.example.a1004bala.data.local.entity.QuoteEntity

@Database(
    entities = [BookEntity::class, QuoteEntity::class],
    version = 1,
    exportSchema = false
)

abstract class BookWormDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun quoteDao(): QuoteDao
    companion object {
        private var instance: BookWormDatabase? = null

        fun getInstance(context: Context): BookWormDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    BookWormDatabase::class.java,
                    "bookworm.db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also { instance = it }
            }
    }
}