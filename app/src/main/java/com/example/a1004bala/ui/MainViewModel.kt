package com.example.a1004bala.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a1004bala.data.preferences.ThemeMode
import com.example.a1004bala.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed interface MainUiState {
    data object Loading : MainUiState
    data class Ready(val themeMode: ThemeMode) : MainUiState
}

class MainViewModel(
    preferences: UserPreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<MainUiState> = preferences.themeMode
        .map<ThemeMode, MainUiState> { mode -> MainUiState.Ready(mode) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MainUiState.Loading
        )
}