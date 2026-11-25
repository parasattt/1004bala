package com.example.a1004bala.ui.screens.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a1004bala.ui.AppViewModelProvider
import com.example.a1004bala.ui.components.ErrorState
import com.example.a1004bala.ui.components.LoadingState
import com.example.a1004bala.ui.components.SearchResultItem
import com.example.a1004bala.ui.components.SearchIllustrations
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
        viewModel::clear, onBack, onResultClick, viewModel::retry,
        requestInitialFocus = query.isBlank() && state == SearchUiState.Idle)
}

@Composable
internal fun SearchContent(
    query: String,
    state: SearchUiState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    onBack: () -> Unit,
    onResultClick: (String) -> Unit,
    onRetry: () -> Unit = onSearch,
    requestInitialFocus: Boolean = false
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focus = LocalFocusManager.current
    val submit: () -> Unit = { keyboard?.hide(); focus.clearFocus(); onSearch() }
    Scaffold(containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(Modifier.statusBarsPadding().padding(start = 18.dp, end = 18.dp, top = 4.dp, bottom = 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                IconButton(onClick = onBack, modifier = Modifier.size(44.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurface)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Артқа", Modifier.size(22.dp))
                }
                SearchField(query, onQueryChange, submit, onClear,
                    Modifier.weight(1f), requestInitialFocus)
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
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("«${query.trim()}» бойынша ${state.books.size} нәтиже",
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                            GoogleBooksAttribution()
                        }
                    }
                    items(state.books, key = { it.id }) { book ->
                        SearchResultItem(book, onAdd = { onResultClick(book.id) })
                    }
                }
                is SearchUiState.Empty -> SearchEmpty(state.query, onClear)
                is SearchUiState.Error -> ErrorState(state.message, onRetry, title = state.title)
            }
        }
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier,
    requestInitialFocus: Boolean
) {
    val interaction = remember { MutableInteractionSource() }
    val focused by interaction.collectIsFocusedAsState()
    val requester = remember { FocusRequester() }
    LaunchedEffect(requestInitialFocus) {
        if (requestInitialFocus) requester.requestFocus()
    }
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.focusRequester(requester),
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 15.sp, fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface),
        cursorBrush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.primary),
        interactionSource = interaction,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        decorationBox = { field ->
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp,
                border = if (focused) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null) {
                Row(Modifier.heightIn(min = 48.dp).padding(start = 4.dp, end = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = onSearch, enabled = query.isNotBlank(), modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.Search, "Іздеу", Modifier.size(22.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Box(Modifier.weight(1f)) {
                        if (query.isEmpty()) Text("Атауы, авторы немесе ISBN",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
                            color = MaterialTheme.colorScheme.outline, maxLines = 1,
                            overflow = TextOverflow.Ellipsis)
                        field()
                    }
                    if (query.isNotEmpty()) IconButton(onClick = onClear, modifier = Modifier.size(32.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                        Icon(Icons.Default.Close, "Іздеуді тазалау", Modifier.size(16.dp))
                    }
                }
            }
        }
    )
}

@Composable
private fun GoogleBooksAttribution() {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Surface(shape = RoundedCornerShape(5.dp), color = MaterialTheme.colorScheme.secondaryContainer) {
            Box(Modifier.size(18.dp), contentAlignment = Alignment.Center) {
                Text("G", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold), color = MaterialTheme.colorScheme.onSecondaryContainer)
            }
        }
        Text("Google Books", style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SearchIdle(onSuggestion: (String) -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(top = 48.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SearchIllustration(SearchIllustrations.Book, MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer)
        Text("Google Books-тан іздеу", style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center)
        Text("Кітаптың атауын, авторын немесе ISBN нөмірін жазыңыз. Табылған кітапты бір басумен сөреге қосасыз.",
            modifier = Modifier.widthIn(max = 262.dp),
            style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        FlowRow(modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Абай жолы", "Atomic Habits", "Есенберлин").forEach { suggestion ->
                Surface(onClick = { onSuggestion(suggestion) }, shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface, shadowElevation = 1.dp) {
                    Row(Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.Search, null, Modifier.size(15.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(suggestion, style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.5.sp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchIllustration(
    image: androidx.compose.ui.graphics.vector.ImageVector,
    container: Color,
    content: Color
) {
    Surface(modifier = Modifier.padding(bottom = 6.dp),
        shape = RoundedCornerShape(30.dp), color = container) {
        Box(Modifier.size(92.dp), contentAlignment = Alignment.Center) {
            Icon(image, null, Modifier.size(40.dp), tint = content)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 760)
@Composable
private fun SearchIdlePreview() {
    BookWormTheme { SearchContent("", SearchUiState.Idle, {}, {}, {}, {}, {}) }
}

@Composable
private fun SearchEmpty(query: String, onClear: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        .padding(start = 8.dp, end = 8.dp, top = 24.dp, bottom = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)) {
        SearchIllustration(SearchIllustrations.EmptySearch, MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant)
        Text("Ештеңе табылмады", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
        Text("«$query» бойынша кітап жоқ. Атауын басқаша жазып көріңіз немесе ISBN енгізіңіз.",
            modifier = Modifier.widthIn(max = 262.dp),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Button(onClick = onClear, modifier = Modifier.padding(top = 8.dp).heightIn(min = 52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurface),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)) {
            Text("Іздеуді тазалау")
        }
    }
}
