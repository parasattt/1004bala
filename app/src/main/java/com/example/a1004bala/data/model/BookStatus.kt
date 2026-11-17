package com.example.a1004bala.data.model

enum class BookStatus(val label: String){
    PLANNED("Жоспарда"),
    READING("Оқуда"),
    FINISHED("Аяқталды");

    companion object{
        val tabOrder: List<BookStatus> = listOf(READING, FINISHED, PLANNED)
    }
}