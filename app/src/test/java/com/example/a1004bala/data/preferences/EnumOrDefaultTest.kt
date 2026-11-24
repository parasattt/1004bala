package com.example.a1004bala.data.preferences

import org.junit.Assert.assertEquals
import org.junit.Test

class EnumOrDefaultTest {

    @Test
    fun nothingSaved_givesDefault() {
        assertEquals(ThemeMode.SYSTEM, enumOrDefault(null, ThemeMode.SYSTEM))
        assertEquals(SortOrder.RECENT, enumOrDefault(null, SortOrder.RECENT))
    }

    @Test
    fun savedName_isRestored() {
        assertEquals(ThemeMode.DARK, enumOrDefault("DARK", ThemeMode.SYSTEM))
        assertEquals(SortOrder.PROGRESS, enumOrDefault("PROGRESS", SortOrder.RECENT))
    }

    @Test
    fun unknownName_givesDefault() {
        assertEquals(ThemeMode.SYSTEM, enumOrDefault("AMOLED", ThemeMode.SYSTEM))
        assertEquals(ThemeMode.SYSTEM, enumOrDefault("", ThemeMode.SYSTEM))
        assertEquals(ThemeMode.SYSTEM, enumOrDefault("dark", ThemeMode.SYSTEM))
    }
}
