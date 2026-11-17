package com.example.a1004bala.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a1004bala.data.model.Quote
import com.example.a1004bala.ui.theme.BookWormTheme

/** Displays a quote; its owner handles deletion through the callback. */
@Composable
fun QuoteItem(
    quote: Quote,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false,
    deleteEnabled: Boolean = true
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = if (highlighted) MaterialTheme.colorScheme.tertiaryContainer
            else MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 12.dp, top = 12.dp, bottom = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "“",
                modifier = Modifier.clearAndSetSemantics { },
                fontFamily = FontFamily.Serif,
                fontSize = 40.sp,
                lineHeight = 32.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = quote.text,
                modifier = Modifier.padding(end = 4.dp),
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (quote.page != null) {
                    Text(
                        text = "${quote.page}-бет",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Spacer(Modifier.weight(1f))
                }
                IconButton(onClick = onDelete, enabled = deleteEnabled) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Дәйексөзді жою",
                        tint = if (deleteEnabled) MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    )
                }
            }
        }
    }
}

@Preview(name = "Quotes · light · with and without page", widthDp = 360, showBackground = true)
@Composable
private fun QuoteItemsLightPreview() {
    BookWormTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuoteItem(
                    quote = Quote(1, bookId = 1, text = "Ғылым таппай мақтанба, орын таппай баптанба.", page = 12),
                    onDelete = {},
                    highlighted = true
                )
                QuoteItem(
                    quote = Quote(2, bookId = 1, text = "Еңбек етсең ерінбей, тояды қарның тіленбей."),
                    onDelete = {}
                )
            }
        }
    }
}

@Preview(name = "Quote · dark · large text", widthDp = 360, fontScale = 1.3f, showBackground = true)
@Composable
private fun QuoteItemDarkPreview() {
    BookWormTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            QuoteItem(
                quote = Quote(
                    1,
                    bookId = 1,
                    text = "Ғылым таппай мақтанба, орын таппай баптанба. Еңбек етсең ерінбей, тояды қарның тіленбей.",
                    page = 45
                ),
                onDelete = {},
                highlighted = true,
                deleteEnabled = false,
                modifier = Modifier.padding(18.dp)
            )
        }
    }
}
