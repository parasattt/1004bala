package com.example.a1004bala.data.preferences

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DataStoreUserPreferencesRepository(
    private val context: Context
) : UserPreferencesRepository {

    // TODO(Dev 4)
    override val themeMode: Flow<ThemeMode> = flowOf(ThemeMode.SYSTEM)
    override val sortOrder: Flow<SortOrder> = flowOf(SortOrder.RECENT)

    override suspend fun setThemeMode(mode: ThemeMode) = Unit
    override suspend fun setSortOrder(order: SortOrder) = Unit
}