package com.example.a1004bala.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a1004bala.ui.theme.BookWormTheme

@Composable
fun SettingsRow(
    icon: ImageVector,
    iconContainer: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    value: String? = null,
    showChevron: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(iconContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = colors.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = colors.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (value != null) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant,
                maxLines = 1
            )
        }
        if (showChevron) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = colors.outline,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(name = "SettingsRow · light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun SettingsRowLightPreview() {
    BookWormTheme(darkTheme = false) { SettingsRowPreviewContent() }
}

@Preview(name = "SettingsRow · dark", showBackground = true, backgroundColor = 0xFF1A1B18)
@Composable
private fun SettingsRowDarkPreview() {
    BookWormTheme(darkTheme = true) { SettingsRowPreviewContent() }
}

@Composable
private fun SettingsRowPreviewContent() {
    val colors = MaterialTheme.colorScheme
    Column {
        SettingsRow(
            icon = Icons.Filled.List,
            iconContainer = colors.primaryContainer,
            iconTint = colors.primary,
            title = "Сұрыптау",
            subtitle = "Кітаптар реті",
            value = "Соңғы қосылған",
            showChevron = true,
            onClick = {}
        )
        SettingsRow(
            icon = Icons.Filled.Info,
            iconContainer = colors.secondaryContainer,
            iconTint = colors.onSecondaryContainer,
            title = "Қосымша туралы",
            subtitle = "BookWorm · 1004bala",
            value = "2.0"
        )
    }
}
