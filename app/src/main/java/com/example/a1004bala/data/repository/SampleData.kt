package com.example.a1004bala.data.repository

import com.example.a1004bala.data.model.Book
import com.example.a1004bala.data.model.BookStatus
import com.example.a1004bala.data.model.Quote

object SampleData {

    val books: List<Book> = listOf(
        Book(1, "Абай жолы", "Мұхтар Әуезов", totalPages = 540, currentPage = 120, status = BookStatus.READING),
        Book(2, "Atomic Habits", "James Clear", totalPages = 320, currentPage = 208, status = BookStatus.READING),
        Book(3, "Көшпенділер", "Ілияс Есенберлин", totalPages = 600, currentPage = 84, status = BookStatus.READING),
        Book(4, "1984", "George Orwell", totalPages = 328, currentPage = 328, status = BookStatus.FINISHED),
        Book(5, "Алхимик", "Пауло Коэльо", totalPages = 208, currentPage = 208, status = BookStatus.FINISHED),
        Book(6, "Sapiens", "Юваль Ной Харари", totalPages = 512, status = BookStatus.PLANNED),
        Book(7, "Қан мен тер", "Әбдіжәміл Нұрпейісов", totalPages = 680, status = BookStatus.PLANNED)
    )

    val quotes: List<Quote> = listOf(
        Quote(1, bookId = 1, text = "Ғылым таппай мақтанба, орын таппай баптанба.", page = 12),
        Quote(2, bookId = 1, text = "Еңбек етсең ерінбей, тояды қарның тіленбей.", page = 45)
    )
}