package com.example.a1004bala.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.a1004bala.data.model.BookStatus
import com.example.a1004bala.ui.theme.BookWormTheme

@Composable
fun StatusTabs(
    selectedStatus: BookStatus,
    counts: Map<BookStatus, Int>,
    onTabSelected: (BookStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    val statuses = BookStatus.tabOrder

    PrimaryTabRow(
        selectedTabIndex = statuses.indexOf(selectedStatus).coerceAtLeast(0),
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        statuses.forEach { status ->
            Tab(
                selected = status == selectedStatus,
                onClick = { onTabSelected(status) },
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                text = {
                    Text(
                        text = "${status.label} (${counts[status] ?: 0})",
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
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
            onTabSelected = {}
        )
    }
}