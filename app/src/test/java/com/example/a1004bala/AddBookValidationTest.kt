package com.example.a1004bala.ui.screens.addbook

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AddBookValidationTest {

    @Test
    fun emptyOrBlankTitle_isError() {
        assertEquals(ERROR_TITLE, validateTitle(""))
        assertEquals(ERROR_TITLE, validateTitle("   "))
    }

    @Test
    fun normalTitle_isOk() {
        assertNull(validateTitle("Абай жолы"))
    }

    @Test
    fun emptyOrBlankAuthor_isError() {
        assertEquals(ERROR_AUTHOR, validateAuthor(""))
        assertEquals(ERROR_AUTHOR, validateAuthor("  "))
    }

    @Test
    fun normalAuthor_isOk() {
        assertNull(validateAuthor("Мұхтар Әуезов"))
    }

    @Test
    fun pages_mustBeNumberGreaterThanZero() {
        assertEquals(ERROR_PAGES, validatePages(""))
        assertEquals(ERROR_PAGES, validatePages("0"))
        assertEquals(ERROR_PAGES, validatePages("-5"))
        assertEquals(ERROR_PAGES, validatePages("abc"))
        assertEquals(ERROR_PAGES, validatePages("12.5"))
    }

    @Test
    fun pages_validNumbers_areOk() {
        assertNull(validatePages("1"))
        assertNull(validatePages("540"))
        assertNull(validatePages(" 320 "))
        assertNull(validatePages("99999"))
    }
}
