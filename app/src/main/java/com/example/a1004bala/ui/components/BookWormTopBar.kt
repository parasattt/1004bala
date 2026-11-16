package com.example.a1004bala.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a1004bala.ui.theme.BookWormTheme

/**
 * @param title
 * @param canNavigateBack
 * @param onBack
 * @param navigationIcon
 * @param containerColor
 * @param actions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookWormTopBar(
    title: String?,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    navigationIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    containerColor: Color = Color.Transparent,
    actions: @Composable RowScope.() -> Unit = {}
){
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            if (title != null) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
            }
        },
        navigationIcon = {
            if (canNavigateBack) {
                TopBarIconButton(
                    icon = navigationIcon,
                    contentDescription = if (navigationIcon == Icons.Filled.Close) "Жабу" else "Артқа",
                    onClick = onBack,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
fun TopBarIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(44.dp),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}


@Preview(name = "Details · light", showBackground = true)
@Composable
private fun TopBarDetailsPreview() {
    BookWormTheme(darkTheme = false) {
        BookWormTopBar(
            title = null,
            canNavigateBack = true,
            actions = {
                TopBarIconButton(
                    icon = Icons.Filled.MoreVert,
                    contentDescription = "Мәртебені өзгерту",
                    onClick = {},
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        )
    }
}

@Preview(name = "AddBook · light", showBackground = true)
@Composable
private fun TopBarAddBookPreview() {
    BookWormTheme(darkTheme = false) {
        BookWormTopBar(
            title = "Жаңа кітап",
            canNavigateBack = true,
            navigationIcon = Icons.Filled.Close
        )
    }
}

@Preview(name = "AddBook · dark", showBackground = true, backgroundColor = 0xFF0F100E)
@Composable
private fun TopBarAddBookDarkPreview() {
    BookWormTheme(darkTheme = true) {
        BookWormTopBar(
            title = "Жаңа кітап",
            canNavigateBack = true,
            navigationIcon = Icons.Filled.Close
        )
    }
}