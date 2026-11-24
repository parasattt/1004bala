package com.example.a1004bala.ui.screens.settings

import com.example.a1004bala.data.preferences.SortOrder
import com.example.a1004bala.data.preferences.ThemeMode

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val sortOrder: SortOrder = SortOrder.RECENT
)
