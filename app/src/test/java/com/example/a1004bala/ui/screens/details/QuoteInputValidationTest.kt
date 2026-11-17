package com.example.a1004bala.ui.screens.details

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class QuoteInputValidationTest {
    @Test
    fun rejectsEmptyAndWhitespaceOnlyQuotes() {
        listOf("", "   ", "\n\t").forEach { assertNotNull(quoteTextError(it)) }
    }

    @Test
    fun acceptsKazakhTextAndMultilineQuotes() {
        assertNull(quoteTextError("Ғылым таппай мақтанба."))
        assertNull(quoteTextError("Бірінші жол\nЕкінші жол"))
    }

    @Test
    fun acceptsUnspecifiedPage() {
        assertNull(quotePageInputError("", 540))
        assertNull(quotePageInputError("   ", 540))
    }

    @Test
    fun acceptsFirstLastAndTrimmedPage() {
        listOf("1", "540", " 12 ").forEach { assertNull(quotePageInputError(it, 540)) }
    }

    @Test
    fun rejectsZeroNegativeAndOutOfRangePages() {
        listOf("0", "-1", "541").forEach { assertNotNull(quotePageInputError(it, 540)) }
    }

    @Test
    fun rejectsNonIntegerAndOverflowPages() {
        listOf("abc", "1.5", "99999999999999999999").forEach {
            assertNotNull(quotePageInputError(it, 540))
        }
    }
}
