package com.example.a1004bala.ui.screens.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import com.example.a1004bala.ui.components.LibraryNavButton
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a1004bala.ui.AppViewModelProvider
import com.example.a1004bala.ui.components.BookCard
import com.example.a1004bala.ui.components.EmptyState
import com.example.a1004bala.ui.components.FinishedBookTile
import com.example.a1004bala.ui.components.SortSheet
import com.example.a1004bala.ui.components.StatusTabs
import com.example.a1004bala.ui.components.TopBarIconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.a1004bala.data.model.Book
import com.example.a1004bala.data.model.BookStatus
import com.example.a1004bala.ui.components.ContinueReadingCard
@Composable
fun LibraryScreen(
    onBookClick: (Int) -> Unit,
    onAddClick: () -> Unit,
    onSearchClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    viewModel: LibraryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val continueBookId = if (uiState.selectedStatus == BookStatus.READING) {
        uiState.books.firstOrNull()?.id
    } else {
        null
    }
    var bookToDelete by remember { mutableStateOf<Book?>(null) }
    var showSortSheet by rememberSaveable { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val finishedGridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var previousSortOrder by remember { mutableStateOf(uiState.sortOrder) }

    LaunchedEffect(uiState.sortOrder) {
        if (previousSortOrder != uiState.sortOrder) {
            listState.requestScrollToItem(0)
            finishedGridState.requestScrollToItem(0)
            previousSortOrder = uiState.sortOrder
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.sortSaveFailures.collect {
            snackbarHostState.showSnackbar("Сұрыптау реті сақталмады. Қайта көріңіз.")
        }
    }

    if (showSortSheet) {
        SortSheet(
            selected = uiState.sortOrder,
            onSelect = { order ->
                viewModel.onSortSelected(order)
                showSortSheet = false
            },
            onDismiss = { showSortSheet = false }
        )
    }

    bookToDelete?.let { book ->
        AlertDialog(
            onDismissRequest = { bookToDelete = null },
            title = { Text("Кітапты жою") },
            text = {
                Text("«${book.title}» кітабы және оның дәйексөздері жойылады.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteBook(book.id)
                        bookToDelete = null
                    }
                ) {
                    Text("Жою", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { bookToDelete = null }) {
                    Text("Бас тарту")
                }
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            if (uiState.books.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = onAddClick,
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null
                        )
                    },
                    text = { Text("Кітап қосу") },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(top = 12.dp, bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Менің сөрем",
                        style = MaterialTheme.typography.displaySmall
                    )
                    Text(
                        text = "${uiState.counts.values.sum()} кітап",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                LibraryNavButton(
                    selected = true,
                    onClick = {
                        if (uiState.books.isNotEmpty()) {
                            scope.launch {
                                if (uiState.selectedStatus == BookStatus.FINISHED) {
                                    finishedGridState.animateScrollToItem(0)
                                } else {
                                    listState.animateScrollToItem(0)
                                }
                            }
                        }
                    }
                )
                TopBarIconButton(
                    icon = Icons.Filled.Search,
                    contentDescription = "Кітап іздеу",
                    onClick = onSearchClick
                )
                TopBarIconButton(
                    icon = Icons.Filled.Settings,
                    contentDescription = "Баптаулар",
                    onClick = onSettingsClick
                )
            }

            StatusTabs(
                selectedStatus = uiState.selectedStatus,
                counts = uiState.counts,
                onTabSelected = viewModel::onTabSelected
            )

            OutlinedButton(
                onClick = { showSortSheet = true },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp)
            ) {
                Text("Сұрыптау: ${uiState.sortOrder.label}")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.books.isEmpty()) {
                EmptyState(
                    onAddClick = onAddClick,
                    modifier = Modifier.weight(1f)
                )
            } else if (uiState.selectedStatus == BookStatus.FINISHED) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    state = finishedGridState,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 96.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item(key = "finished-summary", span = { GridItemSpan(maxLineSpan) }) {
                        FinishedReadingSummary(
                            bookCount = uiState.books.size,
                            pagesRead = uiState.books.sumOf { it.currentPage.toLong() }
                        )
                    }
                    items(items = uiState.books, key = { it.id }) { book ->
                        FinishedBookTile(
                            book = book,
                            onClick = { onBookClick(book.id) },
                            onLongClick = { bookToDelete = book }
                        )
                    }
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(
                        items = uiState.books,
                        key = { book -> book.id }
                    ) { book ->
                        if (book.id == continueBookId) {
                            ContinueReadingCard(
                                book = book,
                                onClick = { onBookClick(book.id) },
                                onLongClick = { bookToDelete = book }
                            )
                        } else {
                            BookCard(
                                book = book,
                                onClick = { onBookClick(book.id) },
                                onLongClick = { bookToDelete = book }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FinishedReadingSummary(bookCount: Int, pagesRead: Long) {
    val colors = MaterialTheme.colorScheme
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = colors.secondaryContainer,
        contentColor = colors.onSecondaryContainer
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = pagesRead.toString(),
                style = MaterialTheme.typography.headlineSmall
            )
            Column(modifier = Modifier.weight(1f)) {
                Text("бет оқылды", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "$bookCount кітап толық аяқталды",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
