package com.example.a1004bala.ui.screens.details

import androidx.lifecycle.SavedStateHandle
import com.example.a1004bala.data.repository.InMemoryBookRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class BookDetailsDraftRestorationTest {
    // No route ID: these tests isolate draft persistence from asynchronous book loading.
    @Test
    fun restoresAllDraftFieldsFromSavedStateSnapshot() {
        val handle = SavedStateHandle()
        val original = BookDetailsViewModel(handle, InMemoryBookRepository())
        original.onPageInputChange("620")
        original.onQuoteInputChange("Бірінші жол\nЕкінші жол")
        original.onQuotePageInputChange(" 12 ")

        val restored = BookDetailsViewModel(snapshot(handle), InMemoryBookRepository())
        assertEquals("620", restored.uiState.value.pageInput)
        assertEquals("Бірінші жол\nЕкінші жол", restored.uiState.value.quoteInput)
        assertEquals(" 12 ", restored.uiState.value.quotePageInput)
        assertFalse(restored.uiState.value.isSaving)
        assertFalse(restored.uiState.value.isAddingQuote)
    }

    @Test
    fun preservesDeliberatelyClearedFields() {
        val handle = SavedStateHandle()
        val original = BookDetailsViewModel(handle, InMemoryBookRepository())
        original.onPageInputChange("120")
        original.onQuoteInputChange("Дәйексөз")
        original.onQuotePageInputChange("12")
        original.onPageInputChange("")
        original.onQuoteInputChange("")
        original.onQuotePageInputChange("")

        val restored = BookDetailsViewModel(snapshot(handle), InMemoryBookRepository())
        assertEquals("", restored.uiState.value.pageInput)
        assertEquals("", restored.uiState.value.quoteInput)
        assertEquals("", restored.uiState.value.quotePageInput)
        assertEquals(3, handle.keys().size)
    }

    @Test
    fun independentNavigationEntriesDoNotShareDrafts() {
        val first = BookDetailsViewModel(SavedStateHandle(), InMemoryBookRepository())
        first.onPageInputChange("300")
        first.onQuoteInputChange("Дәйексөз")
        val second = BookDetailsViewModel(SavedStateHandle(), InMemoryBookRepository())
        assertEquals("", second.uiState.value.pageInput)
        assertEquals("", second.uiState.value.quoteInput)
    }

    private fun snapshot(handle: SavedStateHandle) =
        SavedStateHandle(handle.keys().associateWith { handle.get<Any?>(it) })
}
