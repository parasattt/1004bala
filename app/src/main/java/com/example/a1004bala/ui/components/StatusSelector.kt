package com.example.a1004bala.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a1004bala.data.model.BookStatus
import com.example.a1004bala.ui.theme.BookWormTheme


data class StatusStyle(
    val container: Color,
    val content: Color,
    val icon: ImageVector
)

@Composable
fun BookStatus.statusStyle(): StatusStyle {
    val colors = MaterialTheme.colorScheme
    return when (this) {
        BookStatus.READING -> StatusStyle(
            container = colors.primaryContainer,
            content = colors.primary,
            icon = Icons.Filled.PlayArrow
        )
        BookStatus.FINISHED -> StatusStyle(
            container = colors.secondaryContainer,
            content = colors.onSecondaryContainer,
            icon = Icons.Filled.CheckCircle
        )
        BookStatus.PLANNED -> StatusStyle(
            container = colors.tertiaryContainer,
            content = colors.tertiary,
            icon = Icons.Filled.DateRange
        )
    }
}


@Composable
fun StatusSelector(
    selected: BookStatus,
    onSelect: (BookStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BookStatus.tabOrder.forEach { status ->
            StatusOption(
                status = status,
                isSelected = status == selected,
                onClick = { onSelect(status) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatusOption(
    status: BookStatus,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val style = status.statusStyle()
    val shape = RoundedCornerShape(18.dp)

    Box(
        modifier = modifier
            .clip(shape)
            .background(colors.surface)
            .border(
                width = 2.dp,
                color = if (isSelected) colors.onSurface else Color.Transparent,
                shape = shape
            )
            .selectable(
                selected = isSelected,
                onClick = onClick,
                role = Role.RadioButton
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(style.container),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = style.icon,
                    contentDescription = null,
                    tint = style.content,
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(
                text = status.label,
                style = MaterialTheme.typography.labelMedium,
                color = colors.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(colors.primary)
            )
        }
    }
}

@Preview(name = "StatusSelector · light", showBackground = true, backgroundColor = 0xFFF2F2EE)
@Composable
private fun StatusSelectorLightPreview() {
    BookWormTheme(darkTheme = false) {
        StatusSelectorPreviewContent()
    }
}

@Preview(name = "StatusSelector · dark", showBackground = true, backgroundColor = 0xFF0F100E)
@Composable
private fun StatusSelectorDarkPreview() {
    BookWormTheme(darkTheme = true) {
        StatusSelectorPreviewContent()
    }
}

@Composable
private fun StatusSelectorPreviewContent() {
    var selected by remember { mutableStateOf(BookStatus.PLANNED) }
    StatusSelector(
        selected = selected,
        onSelect = { selected = it },
        modifier = Modifier.padding(18.dp)
    )
}
