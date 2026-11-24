package com.example.a1004bala.ui.screens.addbook

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a1004bala.data.model.Book
import com.example.a1004bala.data.model.BookStatus
import com.example.a1004bala.ui.AppViewModelProvider
import com.example.a1004bala.ui.components.AppTextField
import com.example.a1004bala.ui.components.BookWormTopBar
import com.example.a1004bala.ui.components.LibraryNavButton
import com.example.a1004bala.ui.components.StatusSelector
import com.example.a1004bala.ui.components.statusStyle
import com.example.a1004bala.ui.theme.BookWormTheme


@Composable
fun AddBookScreen(
    onBack: () -> Unit,
    onLibraryClick: () -> Unit = onBack,
    onSearchClick: () -> Unit = {},
    viewModel: AddBookViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()


    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onBack()
    }

    AddBookContent(
        state = state,
        onBack = onBack,
        onLibraryClick = onLibraryClick,
        onTitleChange = viewModel::onTitleChange,
        onAuthorChange = viewModel::onAuthorChange,
        onPagesChange = viewModel::onPagesChange,
        onStatusChange = viewModel::onStatusChange,
        onSave = viewModel::saveBook
    )
}

@Composable
private fun AddBookContent(
    state: AddBookUiState,
    onBack: () -> Unit,
    onLibraryClick: () -> Unit = {},
    onTitleChange: (String) -> Unit,
    onAuthorChange: (String) -> Unit,
    onPagesChange: (String) -> Unit,
    onStatusChange: (BookStatus) -> Unit,
    onSave: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = colors.background,
        topBar = {
            BookWormTopBar(
                title = "Жаңа кітап",
                canNavigateBack = true,
                onBack = onBack,
                navigationIcon = Icons.Filled.Close,
                actions = {
                    LibraryNavButton(
                        onClick = onLibraryClick,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            )
        },
        bottomBar = {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.ime))
                    .padding(horizontal = 18.dp, vertical = 12.dp)
            ) {
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        onSave()
                    },
                    enabled = !state.isSaving,
                    shape = CircleShape,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(text = "Сөреге қосу")
                }
            }
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
            BookPreviewCard(
                title = state.title,
                author = state.author,
                pages = state.totalPages,
                status = state.status
            )

            AppTextField(
                value = state.title,
                onValueChange = onTitleChange,
                label = "Кітап атауы",
                error = state.errors.title,
                placeholder = "Мысалы, «Абай жолы»",
                capitalization = KeyboardCapitalization.Sentences,
                modifier = Modifier.fillMaxWidth()
            )

            AppTextField(
                value = state.author,
                onValueChange = onAuthorChange,
                label = "Авторы",
                error = state.errors.author,
                capitalization = KeyboardCapitalization.Words,
                modifier = Modifier.fillMaxWidth()
            )

            AppTextField(
                value = state.totalPages,
                onValueChange = onPagesChange,
                label = "Беттер саны",
                error = state.errors.totalPages,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
                trailingIcon = {
                    Text(
                        text = "бет",
                        color = colors.outline,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Мәртебе",
                style = MaterialTheme.typography.labelMedium,
                color = colors.onSurfaceVariant
            )
            StatusSelector(
                selected = state.status,
                onSelect = onStatusChange
            )
        }
    }
}


@Composable
private fun BookPreviewCard(
    title: String,
    author: String,
    pages: String,
    status: BookStatus,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val style = status.statusStyle()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(colors.surfaceVariant)
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PreviewCover(title = title)

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "СӨРЕДЕ ОСЫЛАЙ КӨРІНЕДІ",
                style = MaterialTheme.typography.labelSmall,
                color = colors.outline
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = title.ifBlank { "Кітап атауы" },
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                color = if (title.isBlank()) colors.outline else colors.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${author.ifBlank { "Автор" }} · ${pages.ifBlank { "—" }} бет",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(style.container)
                    .padding(horizontal = 11.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(style.content)
                )
                Text(
                    text = status.label,
                    style = MaterialTheme.typography.labelMedium,
                    color = style.content
                )
            }
        }
    }
}


@Composable
private fun PreviewCover(title: String) {
    val outline = MaterialTheme.colorScheme.outline

    if (title.isBlank()) {
        Box(
            modifier = Modifier
                .size(width = 62.dp, height = 93.dp)
                .drawBehind {
                    val stroke = 2.dp.toPx()
                    drawRoundRect(
                        color = outline,
                        topLeft = Offset(stroke / 2, stroke / 2),
                        size = Size(size.width - stroke, size.height - stroke),
                        cornerRadius = CornerRadius(10.dp.toPx()),
                        style = Stroke(
                            width = stroke,
                            pathEffect = PathEffect.dashPathEffect(
                                floatArrayOf(10.dp.toPx(), 8.dp.toPx())
                            )
                        )
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Атауы\nжоқ",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = outline,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Box(
            modifier = Modifier
                .size(width = 62.dp, height = 93.dp)
                .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 10.dp, bottomEnd = 10.dp, bottomStart = 5.dp))
                .background(Color(Book.CoverColors[5]))
                .padding(start = 10.dp, top = 10.dp, end = 6.dp, bottom = 8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 10.5.sp,
                    lineHeight = 11.sp
                ),
                color = Color.White,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(name = "AddBook · заполнено · light", showBackground = true, heightDp = 760)
@Composable
private fun AddBookFilledPreview() {
    BookWormTheme(darkTheme = false) {
        AddBookContent(
            state = AddBookUiState(
                title = "Sapiens",
                author = "Юваль Ной Харари",
                totalPages = "512"
            ),
            onBack = {}, onTitleChange = {}, onAuthorChange = {},
            onPagesChange = {}, onStatusChange = {}, onSave = {}
        )
    }
}

@Preview(name = "AddBook · ошибки · light", showBackground = true, heightDp = 760)
@Composable
private fun AddBookErrorsPreview() {
    BookWormTheme(darkTheme = false) {
        AddBookContent(
            state = AddBookUiState(
                totalPages = "0",
                errors = AddBookErrors(
                    title = ERROR_TITLE,
                    author = ERROR_AUTHOR,
                    totalPages = ERROR_PAGES
                )
            ),
            onBack = {}, onTitleChange = {}, onAuthorChange = {},
            onPagesChange = {}, onStatusChange = {}, onSave = {}
        )
    }
}

@Preview(
    name = "AddBook · заполнено · dark",
    showBackground = true,
    backgroundColor = 0xFF0F100E,
    heightDp = 760
)
@Composable
private fun AddBookDarkPreview() {
    BookWormTheme(darkTheme = true) {
        AddBookContent(
            state = AddBookUiState(
                title = "Sapiens",
                author = "Юваль Ной Харари",
                totalPages = "512",
                status = BookStatus.READING
            ),
            onBack = {}, onTitleChange = {}, onAuthorChange = {},
            onPagesChange = {}, onStatusChange = {}, onSave = {}
        )
    }
}
