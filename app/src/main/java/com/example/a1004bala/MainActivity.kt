package com.example.a1004bala

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a1004bala.ui.theme.BookWormTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var dark by remember { mutableStateOf(false) }
            val systemDark = isSystemInDarkTheme()
            var useSystem by remember { mutableStateOf(true) }
            BookWormTheme(darkTheme = if (useSystem) systemDark else dark) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    DesignCheckScreen(
                        dark = if (useSystem) systemDark else dark,
                        onToggleDark = {
                            useSystem = false
                            dark = it
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun DesignCheckScreen(
    dark: Boolean,
    onToggleDark: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Сәлем, оқырман", style = MaterialTheme.typography.bodyMedium, color = cs.onSurfaceVariant)
                Text("Менің сөрем", style = MaterialTheme.typography.displaySmall)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    if (dark) "Dark" else "Light",
                    style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.width(8.dp))
                Switch(checked = dark, onCheckedChange = onToggleDark)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
                .background(cs.primary)
                .padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(width = 74.dp, height = 110.dp)
                    .clip(RoundedCornerShape(5.dp, 10.dp, 10.dp, 5.dp))
                    .background(Color(0xFF2F6B50))
            )
            Column(Modifier.weight(1f)) {
                Text("ЖАЛҒАСТЫРУ", style = MaterialTheme.typography.labelSmall, color = cs.secondary)
                Text("Абай жолы", style = MaterialTheme.typography.titleLarge, color = cs.onPrimary)
                Text("Мұхтар Әуезов", style = MaterialTheme.typography.bodyMedium, color = cs.onPrimary)
                Spacer(Modifier.height(8.dp))
                Text("120 / 540", style = MaterialTheme.typography.titleMedium, color = cs.onPrimary)
            }
            Box(
                Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(cs.secondary),
                contentAlignment = Alignment.Center
            ) { Text("→", color = cs.onSecondary, style = MaterialTheme.typography.titleMedium) }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(cs.surface)
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                Modifier
                    .size(width = 56.dp, height = 84.dp)
                    .clip(RoundedCornerShape(4.dp, 8.dp, 8.dp, 4.dp))
                    .background(Color(0xFFD0652E))
            )
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Atomic Habits", style = MaterialTheme.typography.titleMedium)
                Text("James Clear", style = MaterialTheme.typography.bodyMedium, color = cs.onSurfaceVariant)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(
                        Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(cs.surfaceVariant)
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth(0.65f)
                                .height(6.dp)
                                .background(cs.primary)
                        )
                    }
                    Text(
                        "65%",
                        style = MaterialTheme.typography.labelMedium,
                        color = cs.primary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(cs.primaryContainer)
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Chip("Оқуда", cs.primaryContainer, cs.primary)
            Chip("Аяқталды", cs.secondaryContainer, cs.onSecondaryContainer)
            Chip("Жоспарда", cs.tertiaryContainer, cs.tertiary)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(onClick = {}) { Text("Сақтау") }
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = cs.surfaceVariant,
                    contentColor = cs.onSurface
                )
            ) { Text("Бас тарту") }
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = cs.error,
                    contentColor = cs.onError
                )
            ) { Text("Жою") }
        }


        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(cs.surface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("displaySmall · Менің сөрем", style = MaterialTheme.typography.displaySmall)
            Text("headlineSmall · Абай жолы", style = MaterialTheme.typography.headlineSmall)
            Text("titleLarge · Кітапты жою керек пе?", style = MaterialTheme.typography.titleLarge)
            Text("titleMedium · Atomic Habits", style = MaterialTheme.typography.titleMedium)
            Text("bodyLarge · Ә Ғ Қ Ң Ө Ұ Ү Һ І ә ғ қ ң ө ұ ү һ і", style = MaterialTheme.typography.bodyLarge)
            Text("bodyMedium · Мұхтар Әуезов · 540 бет", style = MaterialTheme.typography.bodyMedium)
            Text("bodySmall · 420 бет қалды", style = MaterialTheme.typography.bodySmall)
            Text("labelLarge · Сөреге қосу", style = MaterialTheme.typography.labelLarge)
            Text("labelSmall · ЖАЛҒАСТЫРУ", style = MaterialTheme.typography.labelSmall)
        }

        Text("Палитра", style = MaterialTheme.typography.titleLarge)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Swatch("background", cs.background, cs.onBackground)
            Swatch("surface", cs.surface, cs.onSurface)
            Swatch("surfaceVariant", cs.surfaceVariant, cs.onSurfaceVariant)
            Swatch("primary", cs.primary, cs.onPrimary)
            Swatch("primaryContainer", cs.primaryContainer, cs.primary)
            Swatch("secondary", cs.secondary, cs.onSecondary)
            Swatch("secondaryContainer", cs.secondaryContainer, cs.onSecondaryContainer)
            Swatch("tertiary", cs.tertiary, cs.onTertiary)
            Swatch("tertiaryContainer", cs.tertiaryContainer, cs.tertiary)
            Swatch("error", cs.error, cs.onError)
            Swatch("errorContainer", cs.errorContainer, cs.error)
            Swatch("outline", cs.outline, cs.background)
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun Chip(text: String, container: Color, content: Color) {
    Text(
        text,
        style = MaterialTheme.typography.labelMedium,
        color = content,
        modifier = Modifier
            .clip(CircleShape)
            .background(container)
            .padding(horizontal = 11.dp, vertical = 5.dp)
    )
}

@Composable
private fun Swatch(name: String, bg: Color, fg: Color) {
    Box(
        modifier = Modifier
            .size(width = 104.dp, height = 56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
            .padding(8.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Text(name, style = MaterialTheme.typography.bodySmall, color = fg)
    }
}

@Preview(showBackground = true, heightDp = 1800)
@Composable
fun DesignCheckLightPreview() {
    BookWormTheme(darkTheme = false) {
        Box(Modifier.background(MaterialTheme.colorScheme.background)) {
            DesignCheckScreen(dark = false, onToggleDark = {})
        }
    }
}

@Preview(showBackground = true, heightDp = 1800)
@Composable
fun DesignCheckDarkPreview() {
    BookWormTheme(darkTheme = true) {
        Box(Modifier.background(MaterialTheme.colorScheme.background)) {
            DesignCheckScreen(dark = true, onToggleDark = {})
        }
    }
}
