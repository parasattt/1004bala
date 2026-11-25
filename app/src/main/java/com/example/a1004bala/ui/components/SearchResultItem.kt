package com.example.a1004bala.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.a1004bala.data.model.SearchBook

@Composable
fun SearchResultItem(
    book: SearchBook,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
    // Dev 2 supplies BookCover; keeping a slot avoids changing their files.
    cover: @Composable () -> Unit = {
        Surface(
            modifier = Modifier.size(52.dp, 78.dp),
            shape = RoundedCornerShape(5.dp, 10.dp, 10.dp, 5.dp),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(Modifier.padding(6.dp), contentAlignment = Alignment.Center) {
                Text(book.title, style = MaterialTheme.typography.labelSmall,
                    maxLines = 4, overflow = TextOverflow.Ellipsis)
            }
        }
    }
) {
    Surface(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            cover()
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(book.title, style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(book.author, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2,
                    overflow = TextOverflow.Ellipsis)
                Text(listOfNotNull(book.year, book.pageCount?.let { "$it бет" }
                    ?: "бет саны белгісіз").joinToString(" · "),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            FilledIconButton(onClick = onAdd, shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary)) {
                Icon(Icons.Default.Add, contentDescription = "«${book.title}» кітабын қосу")
            }
        }
    }
}
