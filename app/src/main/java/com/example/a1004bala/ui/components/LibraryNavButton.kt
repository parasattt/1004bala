package com.example.a1004bala.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a1004bala.ui.theme.BookWormTheme

/**
 * @param selected
 */
@Composable
fun LibraryNavButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false
) {
    val colors = MaterialTheme.colorScheme
    IconButton(
        onClick = onClick,
        modifier = modifier.size(44.dp),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (selected) colors.primary else colors.surfaceVariant,
            contentColor = if (selected) colors.onPrimary else colors.onSurface
        )
    ) {
        Icon(
            imageVector = LibraryIcon,
            contentDescription = "Менің сөрем"
        )
    }
}

private val LibraryIcon: ImageVector = ImageVector.Builder(
    name = "Library",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(fill = SolidColor(Color.Black), pathFillType = PathFillType.NonZero) {
        moveTo(3.5f, 5f); lineTo(7.5f, 5f); lineTo(7.5f, 18f); lineTo(3.5f, 18f); close()
        moveTo(9.5f, 3f); lineTo(13.5f, 3f); lineTo(13.5f, 18f); lineTo(9.5f, 18f); close()
        moveTo(15.2f, 5.4f); lineTo(19f, 4.4f); lineTo(22.2f, 17.1f); lineTo(18.4f, 18f); close()
        moveTo(2f, 19.5f); lineTo(22f, 19.5f); lineTo(22f, 21.5f); lineTo(2f, 21.5f); close()
    }
}.build()

@Preview(showBackground = true)
@Composable
private fun LibraryNavButtonPreview() {
    BookWormTheme(darkTheme = false) {
        LibraryNavButton(onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun LibraryNavButtonSelectedPreview() {
    BookWormTheme(darkTheme = false) {
        LibraryNavButton(onClick = {}, selected = true)
    }
}
