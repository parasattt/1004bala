package com.example.a1004bala.ui.screens.addbook

import com.example.a1004bala.data.model.BookStatus
import com.example.a1004bala.data.model.SearchBook
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AddBookPrefillTest {

    private fun volume(
        title: String = "Абай жолы",
        author: String = "Мұхтар Әуезов",
        pageCount: Int? = 540,
        coverUrl: String? = "https://example.com/cover.jpg"
    ) = SearchBook(
        id = "abc123",
        title = title,
        author = author,
        pageCount = pageCount,
        year = "1942",
        coverUrl = coverUrl
    )

    @Test
    fun fullVolume_fillsAllFields() {
        val state = AddBookUiState().prefilledFrom(volume())

        assertEquals("Абай жолы", state.title)
        assertEquals("Мұхтар Әуезов", state.author)
        assertEquals("540", state.totalPages)
        assertEquals("https://example.com/cover.jpg", state.coverUrl)
        assertTrue(state.fromGoogleBooks)
        assertFalse(state.pagesMissing)
    }

    @Test
    fun missingPageCount_leavesPagesEmptyAndFlagged() {
        val state = AddBookUiState().prefilledFrom(volume(pageCount = null))

        assertEquals("", state.totalPages)
        assertTrue(state.pagesMissing)
    }

    @Test
    fun zeroPageCount_isTreatedAsMissing() {
        val state = AddBookUiState().prefilledFrom(volume(pageCount = 0))

        assertEquals("", state.totalPages)
        assertTrue(state.pagesMissing)
    }

    @Test
    fun missingCover_staysNull() {
        val state = AddBookUiState().prefilledFrom(volume(coverUrl = null))

        assertNull(state.coverUrl)
    }

    @Test
    fun longTitle_isCutToLimit() {
        val state = AddBookUiState().prefilledFrom(volume(title = "а".repeat(300)))

        assertEquals(MAX_TITLE_LENGTH, state.title.length)
    }

    @Test
    fun chosenStatus_isKept() {
        val state = AddBookUiState(status = BookStatus.READING).prefilledFrom(volume())

        assertEquals(BookStatus.READING, state.status)
    }
}
