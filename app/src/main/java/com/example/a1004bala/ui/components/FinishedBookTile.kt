package com.example.a1004bala.ui.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a1004bala.data.model.Book
import com.example.a1004bala.data.model.BookStatus
import com.example.a1004bala.ui.theme.BookWormTheme

@Composable
fun FinishedBookTile(
    book: Book,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .combinedClickable(
                role = Role.Button,
                onClickLabel = "Кітапты ашу",
                onLongClickLabel = "Кітапты жою",
                onClick = onClick,
                onLongClick = onLongClick
            ),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box {
            BookCover(
                title = book.title,
                author = book.author,
                coverColor = book.coverColor,
                coverUrl = book.coverUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f)
                    .clearAndSetSemantics {}
            )
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                shape = CircleShape,
                color = colors.secondary,
                contentColor = colors.onSecondary
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Аяқталды",
                    modifier = Modifier.padding(6.dp).size(16.dp)
                )
            }
        }

        Text(
            text = book.title,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        val pageCount = "${book.totalPages} бет"
        Text(
            text = if (book.author.isBlank()) pageCount else "${book.author} · $pageCount",
            style = MaterialTheme.typography.bodySmall,
            color = colors.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true, widthDp = 180)
@Composable
private fun FinishedBookTilePreview() {
    BookWormTheme {
        FinishedBookTile(
            book = Book(
                id = 1,
                title = "Абай жолы",
                author = "Мұхтар Әуезов",
                totalPages = 540,
                currentPage = 540,
                status = BookStatus.FINISHED
            ),
            onClick = {},
            onLongClick = {},
            modifier = Modifier.padding(12.dp)
        )
    }
}
