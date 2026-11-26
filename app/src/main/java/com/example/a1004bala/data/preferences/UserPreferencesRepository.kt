package com.example.a1004bala.data.preferences

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val themeMode: Flow<ThemeMode>
    val sortOrder: Flow<SortOrder>

    suspend fun setThemeMode(mode: ThemeMode)
    suspend fun setSortOrder(order: SortOrder)
}