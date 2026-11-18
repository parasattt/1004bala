package com.example.a1004bala.ui.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a1004bala.data.model.Book
import com.example.a1004bala.data.model.BookStatus
import com.example.a1004bala.data.model.Quote
import com.example.a1004bala.ui.AppViewModelProvider
import com.example.a1004bala.ui.components.BookWormTopBar
import com.example.a1004bala.ui.components.LibraryNavButton
import com.example.a1004bala.ui.components.ReadingProgressBar
import com.example.a1004bala.ui.components.QuoteItem
import com.example.a1004bala.ui.theme.BookWormTheme

@Composable
fun BookDetailsScreen(
    onBack: () -> Unit,
    onLibraryClick: () -> Unit = onBack,
    viewModel: BookDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    BookDetailsContent(
        state = state,
        onBack = onBack,
        onLibraryClick = onLibraryClick,
        onPageInputChange = viewModel::onPageInputChange,
        onSavePages = viewModel::updatePages,
        onStepPage = viewModel::stepPage,
        onQuoteInputChange = viewModel::onQuoteInputChange,
        onQuotePageInputChange = viewModel::onQuotePageInputChange,
        onAddQuote = viewModel::addQuote,
        onDeleteQuote = viewModel::deleteQuote,
        onOpenStatusSheet = viewModel::openStatusSheet,
        onSelectStatus = viewModel::selectStatus,
        onDismissStatusSheet = viewModel::dismissStatusSheet,
        onConfirmStatus = viewModel::changeStatus
    )
}

@Composable
private fun BookDetailsContent(
    state: BookDetailsUiState,
    onBack: () -> Unit,
    onLibraryClick: () -> Unit = {},
    onPageInputChange: (String) -> Unit = {},
    onSavePages: () -> Unit = {},
    onStepPage: (Int) -> Unit = {},
    onQuoteInputChange: (String) -> Unit = {},
    onQuotePageInputChange: (String) -> Unit = {},
    onAddQuote: () -> Unit = {},
    onDeleteQuote: (Int) -> Unit = {},
    onOpenStatusSheet: () -> Unit = {},
    onSelectStatus: (BookStatus) -> Unit = {},
    onDismissStatusSheet: () -> Unit = {},
    onConfirmStatus: () -> Unit = {}
) {
    val background = MaterialTheme.colorScheme.background
    val book = state.book
    val headerColor = book?.let { lerp(background, Color(it.coverColor), 0.30f) } ?: background

    Scaffold(
        modifier = Modifier.imePadding(),
        containerColor = background,
        topBar = {
            BookWormTopBar(
                title = if (book == null) "Кітап туралы" else null,
                canNavigateBack = true,
                onBack = onBack,
                containerColor = headerColor,
                actions = {
                    LibraryNavButton(
                        onClick = onLibraryClick,
                        modifier = Modifier.padding(end = if (book != null) 4.dp else 8.dp)
                    )
                    if (book != null) {
                        IconButton(
                            onClick = onOpenStatusSheet,
                            enabled = !state.isSaving && !state.isChangingStatus,
                            modifier = Modifier.padding(end = 8.dp).background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                        ) {
                            Icon(Icons.Filled.MoreVert, contentDescription = "Мәртебені өзгерту")
                        }
                    }
                }
            )
        }
    ) { contentPadding ->
        when {
            state.isLoading -> Box(
                Modifier.fillMaxSize().padding(contentPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            book == null -> Box(
                Modifier.fillMaxSize().padding(contentPadding).padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.actionError ?: "Кітап табылмады",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .background(Brush.verticalGradient(listOf(headerColor, background, background))),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item(key = "book_header") { BookDetailsHeader(book) }
                item(key = "reading_summary") { BookReadingSummary(book) }
                item(key = "page_input") {
                    ReadingPageInput(
                        value = state.pageInput,
                        totalPages = book.totalPages,
                        onValueChange = onPageInputChange,
                        onSave = onSavePages,
                        onStep = onStepPage,
                        isSaving = state.isSaving,
                        error = state.pageError,
                        actionError = state.actionError
                    )
                }
                item(key = "quotes_heading") {
                    QuotesHeading(
                        count = if (state.isQuotesLoading || state.quotesLoadError != null) null else state.quotes.size
                    )
                }
                item(key = "quote_input") {
                    QuoteInputForm(
                        text = state.quoteInput,
                        page = state.quotePageInput,
                        textError = state.quoteError,
                        pageError = state.quotePageError,
                        saveError = state.quoteSaveError,
                        isAdding = state.isAddingQuote,
                        onTextChange = onQuoteInputChange,
                        onPageChange = onQuotePageInputChange,
                        onAdd = onAddQuote
                    )
                }
                if (state.quoteDeleteError != null) {
                    item(key = "quote_delete_error") {
                        Text(
                            text = state.quoteDeleteError,
                            modifier = Modifier.fillMaxWidth().semantics { liveRegion = LiveRegionMode.Polite },
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                when {
                    state.isQuotesLoading -> item(key = "quotes_loading") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            Text("Дәйексөздер жүктелуде…", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    state.quotesLoadError != null -> item(key = "quotes_error") {
                        Text(
                            text = state.quotesLoadError,
                            modifier = Modifier.fillMaxWidth().semantics { liveRegion = LiveRegionMode.Polite },
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    state.quotes.isEmpty() -> item(key = "quotes_empty") {
                        Text(
                            text = "Әзірге дәйексөз жоқ",
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    else -> items(state.quotes, key = { it.id }) { quote ->
                        QuoteItem(
                            quote = quote,
                            onDelete = { onDeleteQuote(quote.id) },
                            highlighted = quote.id == state.quotes.firstOrNull()?.id,
                            deleteEnabled = quote.id !in state.deletingQuoteIds
                        )
                    }
                }
            }
        }
    }
    if (state.isStatusSheetOpen && book != null) {
        StatusSelectionSheet(
            selectedStatus = state.selectedStatus ?: book.status,
            isSaving = state.isChangingStatus,
            error = state.statusError,
            onSelect = onSelectStatus,
            onConfirm = onConfirmStatus,
            onDismiss = onDismissStatusSheet
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatusSelectionSheet(
    selectedStatus: BookStatus,
    isSaving: Boolean,
    error: String?,
    onSelect: (BookStatus) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val saving by rememberUpdatedState(isSaving)
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { value -> value != SheetValue.Hidden || !saving }
    )
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Мәртебені өзгерту",
                modifier = Modifier.semantics { heading() },
                style = MaterialTheme.typography.titleLarge
            )
            Column(
                modifier = Modifier.selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                BookStatus.tabOrder.forEach { status ->
                    StatusOption(
                        status = status,
                        selected = status == selectedStatus,
                        enabled = !isSaving,
                        onClick = { onSelect(status) }
                    )
                }
            }
            if (error != null) {
                Text(
                    text = error,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Button(
                onClick = onConfirm,
                enabled = !isSaving,
                modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
            ) {
                Text(if (isSaving) "Сақталуда…" else "Дайын")
            }
        }
    }
}

@Composable
private fun StatusOption(status: BookStatus, selected: Boolean, enabled: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.5.dp, if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .selectable(selected = selected, enabled = enabled, role = Role.RadioButton, onClick = onClick)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(status.label, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = when (status) {
                        BookStatus.READING -> "Қазір оқып жатырмын"
                        BookStatus.FINISHED -> "Соңына дейін оқылды"
                        BookStatus.PLANNED -> "Кейін оқимын"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            RadioButton(selected = selected, onClick = null, enabled = enabled)
        }
    }
}

@Composable
private fun QuoteInputForm(
    text: String,
    page: String,
    textError: String?,
    pageError: String?,
    saveError: String?,
    isAdding: Boolean,
    onTextChange: (String) -> Unit,
    onPageChange: (String) -> Unit,
    onAdd: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isAdding,
                label = { Text("Дәйексөз") },
                placeholder = { Text("Дәйексөз қосу…") },
                minLines = 2,
                maxLines = 5,
                shape = RoundedCornerShape(16.dp),
                isError = textError != null,
                supportingText = if (textError != null) ({
                    Text(textError, modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
                }) else null
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                OutlinedTextField(
                    value = page,
                    onValueChange = onPageChange,
                    modifier = Modifier.weight(1f),
                    enabled = !isAdding,
                    label = { Text("Бет") },
                    placeholder = { Text("Міндетті емес") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    isError = pageError != null,
                    supportingText = if (pageError != null) ({
                        Text(pageError, modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
                    }) else null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        if (!isAdding) {
                            focusManager.clearFocus()
                            onAdd()
                        }
                    })
                )
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        onAdd()
                    },
                    enabled = !isAdding,
                    modifier = Modifier.padding(top = 8.dp).heightIn(min = 52.dp),
                    shape = CircleShape
                ) {
                    if (isAdding) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text("Қосу")
                    }
                }
            }
            if (saveError != null) {
                Text(
                    text = saveError,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun QuotesHeading(count: Int?) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Дәйексөздер",
            modifier = Modifier.semantics { heading() },
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (count != null) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ) {
                Text(
                    text = count.toString(),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
private fun ReadingPageInput(
    value: String,
    totalPages: Int,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit,
    onStep: (Int) -> Unit,
    isSaving: Boolean,
    error: String?,
    actionError: String?
) {
    val focusManager = LocalFocusManager.current
    val page = value.trim().toIntOrNull()
    val validPage = page != null && page in 0..totalPages
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Қай беттесіз?",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "0 – $totalPages",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving,
                isError = error != null,
                supportingText = if (error != null) ({
                    Text(error, modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
                }) else null,
                label = { Text("Оқылған бет") },
                suffix = { Text("бет") },
                leadingIcon = {
                    PageStepButton(
                        symbol = "−",
                        description = "Бір бетке азайту",
                        enabled = !isSaving && validPage && page > 0,
                        onClick = { onStep(-1) }
                    )
                },
                trailingIcon = {
                    PageStepButton(
                        symbol = "+",
                        description = "Бір бетке көбейту",
                        enabled = !isSaving && validPage && page < totalPages,
                        onClick = { onStep(1) }
                    )
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
                shape = RoundedCornerShape(28.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    if (!isSaving) {
                        focusManager.clearFocus()
                        onSave()
                    }
                })
            )
            if (actionError != null) {
                Text(
                    text = actionError,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Button(
                onClick = {
                    focusManager.clearFocus()
                    onSave()
                },
                enabled = !isSaving,
                modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                shape = CircleShape
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(if (isSaving) "Сақталуда…" else "Сақтау")
                }
            }
        }
    }
}

@Composable
private fun PageStepButton(
    symbol: String,
    description: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.size(48.dp).semantics { contentDescription = description }
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            contentColor = if (enabled) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        ) {
            Box(modifier = Modifier.size(36.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = symbol,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.clearAndSetSemantics { }
                )
            }
        }
    }
}

@Composable
private fun BookReadingSummary(book: Book) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp).height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Surface(
            modifier = Modifier.weight(1.15f).fillMaxHeight(),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                ReadingProgressBar(progress = book.progress, percent = book.progressPercent)
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PageCountTile(count = book.currentPage, label = "оқылған бет")
            PageCountTile(count = book.pagesLeft, label = "бет қалды")
        }
    }
}

@Composable
private fun PageCountTile(count: Int, label: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun BookDetailsHeader(book: Book) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DetailsBookCover(book)
        Text(
            text = book.title,
            modifier = Modifier.padding(top = 12.dp).semantics { heading() },
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "${book.author} · ${book.totalPages} бет",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        BookStatusLabel(book.status)
    }
}

@Composable
private fun DetailsBookCover(book: Book) {
    val coverColor = Color(book.coverColor)
    Surface(
        modifier = Modifier.size(width = 124.dp, height = 186.dp),
        shape = RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp, topEnd = 10.dp, bottomEnd = 10.dp),
        color = coverColor,
        shadowElevation = 12.dp
    ) {
        Box(
            Modifier.background(
                Brush.linearGradient(listOf(lerp(coverColor, Color.White, 0.12f), coverColor, lerp(coverColor, Color.Black, 0.35f)))
            )
        ) {
            Box(
                Modifier.fillMaxSize().background(
                    Brush.horizontalGradient(
                        0f to Color.Black.copy(alpha = 0.25f),
                        0.06f to Color.White.copy(alpha = 0.10f),
                        0.09f to Color.Transparent
                    )
                )
            )
            Column(
                modifier = Modifier.fillMaxSize().padding(start = 20.dp, end = 12.dp, top = 24.dp, bottom = 18.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    letterSpacing = 0.5.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun BookStatusLabel(status: BookStatus) {
    val colors = MaterialTheme.colorScheme
    val (container, content) = when (status) {
        BookStatus.READING -> colors.primaryContainer to colors.onPrimaryContainer
        BookStatus.FINISHED -> colors.secondaryContainer to colors.onSecondaryContainer
        BookStatus.PLANNED -> colors.tertiaryContainer to colors.onTertiaryContainer
    }
    Surface(shape = CircleShape, color = container, contentColor = content) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(Modifier.size(7.dp).background(content, CircleShape))
            Text(text = status.label, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Preview(name = "Book header · light", widthDp = 360, heightDp = 760, showBackground = true)
@Composable
private fun BookDetailsHeaderLightPreview() {
    BookWormTheme(darkTheme = false) {
        BookDetailsContent(state = headerPreviewState(), onBack = {})
    }
}

@Preview(name = "Book header · dark", widthDp = 360, heightDp = 760, showBackground = true)
@Composable
private fun BookDetailsHeaderDarkPreview() {
    BookWormTheme(darkTheme = true) {
        BookDetailsContent(state = headerPreviewState(), onBack = {})
    }
}

@Preview(name = "Book not found", widthDp = 360, heightDp = 760, showBackground = true)
@Composable
private fun BookDetailsMissingPreview() {
    BookWormTheme {
        BookDetailsContent(state = BookDetailsUiState(isLoading = false), onBack = {})
    }
}

@Preview(name = "Page input · invalid range", widthDp = 360, heightDp = 760, showBackground = true)
@Composable
private fun BookDetailsPageErrorPreview() {
    BookWormTheme(darkTheme = false) {
        BookDetailsContent(
            state = headerPreviewState().copy(
                pageInput = "620",
                pageError = pageInputError("620", 540)
            ),
            onBack = {}
        )
    }
}

@Preview(name = "Quote form · validation errors", widthDp = 360, showBackground = true)
@Composable
private fun QuoteInputErrorPreview() {
    BookWormTheme(darkTheme = false) {
        QuoteInputForm(
            text = "",
            page = "541",
            textError = quoteTextError(""),
            pageError = quotePageInputError("541", 540),
            saveError = null,
            isAdding = false,
            onTextChange = {},
            onPageChange = {},
            onAdd = {}
        )
    }
}

@Preview(name = "Status options · light", widthDp = 360, showBackground = true)
@Composable
private fun StatusOptionsPreview() {
    BookWormTheme(darkTheme = false) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            BookStatus.tabOrder.forEach { status ->
                StatusOption(status = status, selected = status == BookStatus.READING, enabled = true, onClick = {})
            }
        }
    }
}

private fun headerPreviewState() = BookDetailsUiState(
    isLoading = false,
    isQuotesLoading = false,
    quotes = listOf(
        Quote(1, bookId = 1, text = "Ғылым таппай мақтанба, орын таппай баптанба.", page = 12),
        Quote(2, bookId = 1, text = "Еңбек етсең ерінбей, тояды қарның тіленбей.", page = 45)
    ),
    pageInput = "120",
    book = Book(
        id = 1,
        title = "Абай жолы",
        author = "Мұхтар Әуезов",
        totalPages = 540,
        currentPage = 120,
        status = BookStatus.READING,
        coverColor = 0xFF2F6B50
    )
)
