package com.example.a1004bala.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ErrorState(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier,
    title: String = "Іздеу орындалмады") {
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(start = 8.dp, end = 8.dp, top = 24.dp, bottom = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)) {
        Surface(modifier = Modifier.padding(bottom = 6.dp), shape = RoundedCornerShape(30.dp),
            color = MaterialTheme.colorScheme.errorContainer) {
            Box(Modifier.size(92.dp), contentAlignment = Alignment.Center) {
                Icon(SearchIllustrations.Offline, null, Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onErrorContainer)
            }
        }
        Text(title, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
        Text(message, modifier = Modifier.widthIn(max = 262.dp).semantics { liveRegion = LiveRegionMode.Polite },
            style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Button(onClick = onRetry, modifier = Modifier.padding(top = 8.dp).heightIn(min = 52.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)) {
            Icon(SearchIllustrations.Retry, null, Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Қайталау")
        }
    }
}
