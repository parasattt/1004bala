package com.example.a1004bala.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a1004bala.data.preferences.SortOrder
import com.example.a1004bala.data.preferences.ThemeMode
import com.example.a1004bala.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val prefs: UserPreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        prefs.themeMode,
        prefs.sortOrder
    ) { theme, sort ->
        SettingsUiState(themeMode = theme, sortOrder = sort)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState()
    )

    fun onThemeSelected(mode: ThemeMode) {
        viewModelScope.launch { prefs.setThemeMode(mode) }
    }

    fun onSortSelected(order: SortOrder) {
        viewModelScope.launch { prefs.setSortOrder(order) }
    }
}
