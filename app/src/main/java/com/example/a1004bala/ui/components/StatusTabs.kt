package com.example.a1004bala.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a1004bala.data.model.BookStatus
import com.example.a1004bala.ui.theme.BookWormTheme

@Composable
fun StatusTabs(
    selectedStatus: BookStatus,
    counts: Map<BookStatus, Int>,
    onTabSelected: (BookStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BookStatus.tabOrder.forEach { status ->
            val selected = status == selectedStatus
            val contentColor = if (selected) colors.onPrimary else colors.onSurfaceVariant

            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .clip(CircleShape)
                    .background(if (selected) colors.primary else colors.surface)
                    .selectable(
                        selected = selected,
                        role = Role.Tab,
                        onClick = { onTabSelected(status) }
                    )
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = status.label,
                    style = MaterialTheme.typography.labelMedium,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Text(
                    text = "${counts[status] ?: 0}",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (selected) colors.onSecondary else colors.onSurfaceVariant,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            if (selected) colors.secondary
                            else colors.surfaceVariant
                        )
                        .padding(horizontal = 7.dp, vertical = 1.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF2F2EE)
@Composable
private fun StatusTabsPreview() {
    BookWormTheme {
        StatusTabs(
            selectedStatus = BookStatus.READING,
            counts = mapOf(
                BookStatus.READING to 3,
                BookStatus.FINISHED to 2,
                BookStatus.PLANNED to 1
            ),
            onTabSelected = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
