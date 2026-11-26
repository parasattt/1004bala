package com.example.a1004bala.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a1004bala.data.preferences.SortOrder
import com.example.a1004bala.data.preferences.ThemeMode
import com.example.a1004bala.ui.AppViewModelProvider
import com.example.a1004bala.ui.components.BookWormTopBar
import com.example.a1004bala.ui.components.SettingsRow
import com.example.a1004bala.ui.components.SortSheet
import com.example.a1004bala.ui.components.ThemeSelector
import com.example.a1004bala.ui.theme.BookWormTheme

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(viewModel) {
        viewModel.sortSaveFailures.collect {
            snackbarHostState.showSnackbar("Сұрыптау реті сақталмады. Қайта көріңіз.")
        }
    }

    SettingsContent(
        state = state,
        onBack = onBack,
        onThemeSelected = viewModel::onThemeSelected,
        onSortSelected = viewModel::onSortSelected,
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    onBack: () -> Unit,
    onThemeSelected: (ThemeMode) -> Unit,
    onSortSelected: (SortOrder) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val colors = MaterialTheme.colorScheme
    var showSortSheet by rememberSaveable { mutableStateOf(false) }

    if (showSortSheet) {
        SortSheet(
            selected = state.sortOrder,
            onSelect = {
                onSortSelected(it)
                showSortSheet = false
            },
            onDismiss = { showSortSheet = false }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = colors.background,
        topBar = {
            BookWormTopBar(
                title = "Баптаулар",
                canNavigateBack = true,
                onBack = onBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
                .padding(top = 4.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            SectionLabel("КӨРІНІС")
            ThemeSelector(
                selected = state.themeMode,
                onSelect = onThemeSelected
            )
            SavedNote("Таңдау құрылғыда сақталады. Қосымшаны жауып, қайта ашсаңыз да өзгермейді.")

            SectionLabel("СӨРЕ")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(colors.surface)
            ) {
                SettingsRow(
                    icon = Icons.Filled.List,
                    iconContainer = colors.primaryContainer,
                    iconTint = colors.primary,
                    title = "Сұрыптау",
                    subtitle = "Кітаптар реті",
                    value = state.sortOrder.label,
                    onClick = { showSortSheet = true }
                )
                RowDivider()
                SettingsRow(
                    icon = Icons.Filled.Lock,
                    iconContainer = colors.tertiaryContainer,
                    iconTint = colors.tertiary,
                    title = "Деректер",
                    subtitle = "Кітаптар мен дәйексөздер осы құрылғыда"
                )
                RowDivider()
                SettingsRow(
                    icon = Icons.Filled.Info,
                    iconContainer = colors.secondaryContainer,
                    iconTint = colors.onSecondaryContainer,
                    title = "Қосымша туралы",
                    subtitle = "BookWorm · 1004bala",
                    value = "2.0"
                )
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.outline,
        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
    )
}

@Composable
private fun SavedNote(text: String) {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colors.surfaceVariant)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Filled.Info,
            contentDescription = null,
            tint = colors.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = colors.onSurfaceVariant
        )
    }
}

@Composable
private fun RowDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 66.dp)
            .height(1.dp)
            .background(MaterialTheme.colorScheme.outlineVariant)
    )
}

@Preview(name = "Settings · system · light", showBackground = true, heightDp = 760)
@Composable
private fun SettingsLightPreview() {
    BookWormTheme(darkTheme = false) {
        SettingsContent(
            state = SettingsUiState(),
            onBack = {},
            onThemeSelected = {},
            onSortSelected = {}
        )
    }
}

@Preview(
    name = "Settings · dark selected",
    showBackground = true,
    backgroundColor = 0xFF0F100E,
    heightDp = 760
)
@Composable
private fun SettingsDarkPreview() {
    BookWormTheme(darkTheme = true) {
        SettingsContent(
            state = SettingsUiState(
                themeMode = ThemeMode.DARK,
                sortOrder = SortOrder.PROGRESS
            ),
            onBack = {},
            onThemeSelected = {},
            onSortSelected = {}
        )
    }
}
