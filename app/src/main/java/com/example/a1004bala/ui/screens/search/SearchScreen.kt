package com.example.a1004bala.ui.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a1004bala.ui.AppViewModelProvider
import com.example.a1004bala.ui.components.LoadingState
import com.example.a1004bala.ui.components.SearchResultItem
import com.example.a1004bala.ui.theme.BookWormTheme

@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onResultClick: (volumeId: String) -> Unit,
    viewModel: SearchViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SearchContent(query, state, viewModel::onQueryChange, viewModel::search,
        viewModel::clear, onBack, onResultClick)
}

@Composable
internal fun SearchContent(
    query: String,
    state: SearchUiState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    onBack: () -> Unit,
    onResultClick: (String) -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val submit: () -> Unit = { keyboard?.hide(); onSearch() }
    Scaffold(containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(Modifier.statusBarsPadding().padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilledTonalIconButton(onClick = onBack, shape = CircleShape) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Артқа")
                }
                OutlinedTextField(value = query, onValueChange = onQueryChange,
                    modifier = Modifier.weight(1f), singleLine = true,
                    shape = RoundedCornerShape(28.dp),
                    placeholder = { Text("Атауы, авторы немесе ISBN", style = MaterialTheme.typography.bodySmall) },
                    leadingIcon = {
                        IconButton(onClick = submit, enabled = query.isNotBlank()) {
                            Icon(Icons.Default.Search, contentDescription = "Іздеу")
                        }
                    },
                    trailingIcon = { if (query.isNotEmpty()) IconButton(onClick = onClear) {
                        Icon(Icons.Default.Close, contentDescription = "Іздеуді тазалау")
                    } },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { submit() }))
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding).imePadding().padding(horizontal = 18.dp)) {
            when (state) {
                SearchUiState.Idle -> SearchIdle { value -> onQueryChange(value); submit() }
                SearchUiState.Loading -> LoadingState()
                is SearchUiState.Success -> LazyColumn(
                    contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    item {
                        Text("«${query.trim()}» бойынша ${state.books.size} нәтиже · Google Books",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    items(state.books, key = { it.id }) { book ->
                        SearchResultItem(book, onAdd = { onResultClick(book.id) })
                    }
                }
                is SearchUiState.Empty -> Text("Ештеңе табылмады", Modifier.align(Alignment.Center))
                is SearchUiState.Error -> Text(state.message, Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
private fun SearchIdle(onSuggestion: (String) -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(top = 40.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Surface(shape = RoundedCornerShape(28.dp), color = MaterialTheme.colorScheme.secondaryContainer) {
            Icon(Icons.Default.Search, contentDescription = null,
                modifier = Modifier.padding(24.dp).size(40.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer)
        }
        Text("Google Books-тан іздеу", style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center)
        Text("Кітаптың атауын, авторын немесе ISBN нөмірін жазыңыз. Табылған кітапты сөреге қосыңыз.",
            style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        listOf("Абай жолы", "Atomic Habits", "Есенберлин").forEach { suggestion ->
            SuggestionChip(onClick = { onSuggestion(suggestion) }, label = { Text(suggestion) },
                icon = { Icon(Icons.Default.Search, null, Modifier.size(16.dp)) })
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 760)
@Composable
private fun SearchIdlePreview() {
    BookWormTheme { SearchContent("", SearchUiState.Idle, {}, {}, {}, {}, {}) }
}
