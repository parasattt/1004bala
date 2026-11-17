package com.example.a1004bala.ui.screens.details

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class PageInputValidationTest {
    @Test
    fun acceptsUnreadAndCompletedBoundaries() {
        assertNull(pageInputError("0", 540))
        assertNull(pageInputError("540", 540))
    }

    @Test
    fun acceptsIntermediatePageAndTrimsWhitespace() {
        assertNull(pageInputError("120", 540))
        assertNull(pageInputError(" 120 ", 540))
    }

    @Test
    fun rejectsPagesOutsideBook() {
        assertNotNull(pageInputError("-1", 540))
        assertNotNull(pageInputError("541", 540))
    }

    @Test
    fun rejectsEmptyTextAndNonIntegerInput() {
        listOf("", "   ", "abc", "12.5").forEach {
            assertNotNull(pageInputError(it, 540))
        }
    }

    @Test
    fun rejectsOverflowWithoutThrowing() {
        assertNotNull(pageInputError("99999999999999999999", 540))
    }

    @Test
    fun validatesAgainstActualBookLength() {
        assertNull(pageInputError("1", 1))
        assertNotNull(pageInputError("2", 1))
    }
}
