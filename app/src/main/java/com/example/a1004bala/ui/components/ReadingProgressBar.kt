package com.example.a1004bala.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a1004bala.ui.theme.BookWormTheme

@Composable
fun ReadingProgressBar(
    progress: Float,
    percent: Int,
    modifier: Modifier = Modifier
) {
    val safeProgress = progress.coerceIn(0f, 1f)
    val safePercent = percent.coerceIn(0, 100)
    Box(
        modifier = modifier
            .size(104.dp)
            .clearAndSetSemantics {
                progressBarRangeInfo = ProgressBarRangeInfo(safeProgress, 0f..1f)
                stateDescription = "$safePercent% оқылды"
            },
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { safeProgress },
            modifier = Modifier.matchParentSize(),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeWidth = 10.dp,
            strokeCap = StrokeCap.Round
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "$safePercent%",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "оқылды",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(name = "Progress · 22%", showBackground = true)
@Composable
private fun ReadingProgressPreview() {
    BookWormTheme(darkTheme = false) {
        Surface { ReadingProgressBar(progress = 120f / 540, percent = 22, modifier = Modifier.padding(16.dp)) }
    }
}

@Preview(name = "Progress · empty", showBackground = true)
@Composable
private fun ReadingProgressEmptyPreview() {
    BookWormTheme(darkTheme = false) {
        Surface { ReadingProgressBar(progress = 0f, percent = 0, modifier = Modifier.padding(16.dp)) }
    }
}

@Preview(name = "Progress · complete · dark", showBackground = true)
@Composable
private fun ReadingProgressCompletePreview() {
    BookWormTheme(darkTheme = true) {
        Surface { ReadingProgressBar(progress = 1f, percent = 100, modifier = Modifier.padding(16.dp)) }
    }
}
