package com.example.a1004bala.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

// Outline illustrations from the BookWorm v3 search mockups.
internal object SearchIllustrations {
    val Book = outline("Open book") {
        moveTo(2f, 5f); horizontalLineTo(8f)
        curveTo(10.2f, 5f, 12f, 6.8f, 12f, 9f)
        verticalLineTo(20f)
        curveTo(12f, 18.3f, 10.7f, 17f, 9f, 17f)
        horizontalLineTo(2f); close()
        moveTo(22f, 5f); horizontalLineTo(16f)
        curveTo(13.8f, 5f, 12f, 6.8f, 12f, 9f)
        verticalLineTo(20f)
        curveTo(12f, 18.3f, 13.3f, 17f, 15f, 17f)
        horizontalLineTo(22f); close()
    }

    val EmptySearch = outline("No search results") {
        moveTo(18f, 11f)
        arcTo(7f, 7f, 0f, true, true, 4f, 11f)
        arcTo(7f, 7f, 0f, true, true, 18f, 11f)
        moveTo(16.5f, 16.5f); lineTo(20f, 20f)
        moveTo(8.5f, 9f); lineTo(13.5f, 13f)
        moveTo(13.5f, 9f); lineTo(8.5f, 13f)
    }

    val Offline = outline("Connection unavailable") {
        moveTo(2f, 2f); lineTo(22f, 22f)
        moveTo(8.5f, 16.4f); curveTo(10.4f, 14.6f, 13.6f, 14.6f, 15.5f, 16.4f)
        moveTo(5f, 12.9f); curveTo(6.5f, 11.4f, 8.3f, 10.4f, 10.2f, 10.1f)
        moveTo(19f, 12.9f); lineTo(17.1f, 11.4f)
        moveTo(2f, 8.8f); curveTo(3.3f, 7.6f, 4.7f, 6.7f, 6.2f, 6.1f)
        moveTo(22f, 8.8f); curveTo(18.8f, 6.1f, 14.8f, 4.8f, 10.7f, 5.1f)
        moveTo(12f, 19.8f); lineTo(12f, 20.2f)
    }

    val Retry = outline("Retry") {
        moveTo(20f, 7f); verticalLineTo(3f)
        moveTo(20f, 7f); horizontalLineTo(16f)
        moveTo(20f, 7f)
        curveTo(17f, 2f, 9f, 2f, 5.4f, 6.8f)
        curveTo(1.8f, 11.6f, 4.1f, 18.5f, 9.8f, 20.1f)
        curveTo(14f, 21.3f, 18.5f, 19f, 20f, 15f)
    }

    private fun outline(name: String, block: PathBuilder.() -> Unit): ImageVector =
        ImageVector.Builder(name, 40.dp, 40.dp, 24f, 24f).apply {
            path(fill = null, stroke = SolidColor(Color.Black), strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round,
                pathBuilder = block)
        }.build()
}
