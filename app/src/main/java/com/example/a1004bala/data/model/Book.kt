package com.example.a1004bala.data.model

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val totalPages: Int,
    val currentPage: Int = 0,
    val status: BookStatus = BookStatus.PLANNED,
    val coverColor: Long = coverColorFor(id),
    val coverUrl: String? = null,
){
    init {
        require(totalPages > 0) { "totalPages must be > 0" }
        require(currentPage in 0..totalPages) { "currentPage must be >= 0" }
    }
    val progress: Float
        get() = currentPage.toFloat()/totalPages

    val progressPercent: Int
        get() = (progress * 100).toInt()

    val pagesLeft: Int
        get() = totalPages - currentPage
    companion object{
        val CoverColors: List<Long> = listOf(
            0xFF2F6B50, // зелёный   — «Абай жолы»
            0xFFD0652E, // оранжевый — «Atomic Habits»
            0xFF34598C, // синий     — «Көшпенділер»
            0xFF8C2F33, // бордовый  — «1984»
            0xFFB4882B, // охра      — «Алхимик»
            0xFF55468A, // фиолетовый — «Sapiens»
            0xFF4F6F52  // оливковый
        )

        fun coverColorFor(id: Int): Long =
            CoverColors[Math.floorMod(id, CoverColors.size)]
    }
}