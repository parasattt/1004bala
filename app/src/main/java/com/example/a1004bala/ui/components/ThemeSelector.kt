package com.example.a1004bala.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a1004bala.data.preferences.ThemeMode
import com.example.a1004bala.ui.theme.BgDark
import com.example.a1004bala.ui.theme.BgLight
import com.example.a1004bala.ui.theme.BookWormTheme
import com.example.a1004bala.ui.theme.OutlineVariantDark
import com.example.a1004bala.ui.theme.OutlineVariantLight
import com.example.a1004bala.ui.theme.PrimaryDark
import com.example.a1004bala.ui.theme.PrimaryLight
import com.example.a1004bala.ui.theme.SurfaceDark
import com.example.a1004bala.ui.theme.SurfaceLight
import kotlin.math.cos
import kotlin.math.sin

val ThemeMode.label: String
    get() = when (this) {
        ThemeMode.SYSTEM -> "Жүйелік"
        ThemeMode.LIGHT -> "Жарық"
        ThemeMode.DARK -> "Қараңғы"
    }

@Composable
fun ThemeSelector(
    selected: ThemeMode,
    onSelect: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ThemeMode.entries.forEach { mode ->
            ThemeOption(
                mode = mode,
                isSelected = mode == selected,
                onClick = { onSelect(mode) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ThemeOption(
    mode: ThemeMode,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(20.dp)

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
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ThemePreview(
                mode = mode,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ThemeGlyph(mode = mode, tint = colors.onSurface)
                Text(
                    text = mode.label,
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.onSurface,
                    maxLines = 1
                )
            }
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(colors.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = colors.onPrimary,
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}

private class MiniPalette(
    val background: Color,
    val title: Color,
    val card: Color,
    val line: Color
)

private val LightMini = MiniPalette(BgLight, PrimaryLight, SurfaceLight, OutlineVariantLight)
private val DarkMini = MiniPalette(BgDark, PrimaryDark, SurfaceDark, OutlineVariantDark)

@Composable
private fun ThemePreview(mode: ThemeMode, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        when (mode) {
            ThemeMode.LIGHT -> drawMini(LightMini)
            ThemeMode.DARK -> drawMini(DarkMini)
            ThemeMode.SYSTEM -> {
                drawMini(LightMini)
                val triangle = Path().apply {
                    moveTo(size.width, 0f)
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                }
                clipPath(triangle) { drawMini(DarkMini) }
            }
        }
    }
}

private fun DrawScope.drawMini(p: MiniPalette) {
    val pad = 8.dp.toPx()
    val w = size.width - pad * 2
    drawRect(color = p.background)
    drawRoundRect(
        color = p.title,
        topLeft = Offset(pad, pad),
        size = Size(w * 0.64f, 12.dp.toPx()),
        cornerRadius = CornerRadius(5.dp.toPx())
    )
    drawRoundRect(
        color = p.card,
        topLeft = Offset(pad, pad + 12.dp.toPx() + 5.dp.toPx()),
        size = Size(w, 16.dp.toPx()),
        cornerRadius = CornerRadius(5.dp.toPx())
    )
    drawRoundRect(
        color = p.line,
        topLeft = Offset(pad, pad + 12.dp.toPx() + 5.dp.toPx() + 16.dp.toPx() + 5.dp.toPx()),
        size = Size(w * 0.8f, 6.dp.toPx()),
        cornerRadius = CornerRadius(3.dp.toPx())
    )
}

@Composable
private fun ThemeGlyph(mode: ThemeMode, tint: Color) {
    when (mode) {
        ThemeMode.SYSTEM -> Icon(
            imageVector = Icons.Filled.Phone,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(16.dp)
        )
        ThemeMode.LIGHT -> Canvas(Modifier.size(16.dp)) {
            val r = size.minDimension * 0.22f
            drawCircle(color = tint, radius = r, center = center)
            repeat(8) { i ->
                val angle = Math.toRadians(i * 45.0)
                val dx = cos(angle).toFloat()
                val dy = sin(angle).toFloat()
                drawLine(
                    color = tint,
                    start = Offset(center.x + dx * r * 1.6f, center.y + dy * r * 1.6f),
                    end = Offset(center.x + dx * r * 2.2f, center.y + dy * r * 2.2f),
                    strokeWidth = 1.5.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        ThemeMode.DARK -> Canvas(Modifier.size(16.dp)) {
            val r = size.minDimension * 0.42f
            val full = Path().apply { addOval(Rect(center = center, radius = r)) }
            val bite = Path().apply {
                addOval(Rect(center = Offset(center.x + r * 0.55f, center.y - r * 0.45f), radius = r * 0.8f))
            }
            drawPath(Path.combine(PathOperation.Difference, full, bite), color = tint)
        }
    }
}

@Preview(name = "ThemeSelector · light", showBackground = true, backgroundColor = 0xFFF2F2EE)
@Composable
private fun ThemeSelectorLightPreview() {
    BookWormTheme(darkTheme = false) {
        ThemeSelectorPreviewContent()
    }
}

@Preview(name = "ThemeSelector · dark", showBackground = true, backgroundColor = 0xFF0F100E)
@Composable
private fun ThemeSelectorDarkPreview() {
    BookWormTheme(darkTheme = true) {
        ThemeSelectorPreviewContent(start = ThemeMode.DARK)
    }
}

@Composable
private fun ThemeSelectorPreviewContent(start: ThemeMode = ThemeMode.SYSTEM) {
    var selected by remember { mutableStateOf(start) }
    ThemeSelector(
        selected = selected,
        onSelect = { selected = it },
        modifier = Modifier.padding(18.dp)
    )
}
