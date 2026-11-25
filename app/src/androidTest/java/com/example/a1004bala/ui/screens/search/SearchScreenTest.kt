package com.example.a1004bala.ui.screens.search

import android.graphics.Bitmap
import android.graphics.Canvas
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import androidx.activity.compose.setContent
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.example.a1004bala.MainActivity
import com.example.a1004bala.data.model.SearchBook
import com.example.a1004bala.ui.theme.BookWormTheme
import java.io.File
import org.junit.Assert.assertTrue
import org.junit.Test

// Native instrumentation avoids the old Espresso version's incompatibility with API 37.
class SearchScreenTest {
    @Test fun renderAllStatesInBothThemes() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val book = SearchBook("volume-1", "Абай жолы", "Мұхтар Әуезов", 540, "1942", null)
        val states = listOf(SearchUiState.Idle, SearchUiState.Loading,
            SearchUiState.Success(listOf(book, book.copy(id = "2", title = "Қара сөздер", pageCount = null))),
            SearchUiState.Empty("абай"), SearchUiState.Error("Интернет байланысын тексеріңіз де, қайта көріңіз."))
        val directory = File(instrumentation.targetContext.getExternalFilesDir(null), "search-audit")
        directory.mkdirs()
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            for (dark in listOf(false, true)) for ((index, state) in states.withIndex()) {
                scenario.onActivity { activity ->
                    activity.setContent {
                        BookWormTheme(darkTheme = dark) {
                            SearchContent("абай", state, {}, {}, {}, {}, {})
                        }
                    }
                }
                instrumentation.waitForIdleSync()
                val rendered = CountDownLatch(1)
                scenario.onActivity { activity ->
                    activity.window.decorView.postOnAnimation {
                        activity.window.decorView.postOnAnimation {
                            activity.window.decorView.postOnAnimation { rendered.countDown() }
                        }
                    }
                }
                assertTrue("Compose must draw the requested state", rendered.await(10, TimeUnit.SECONDS))
                instrumentation.waitForIdleSync()
                // Capture this app's rendered view, excluding unrelated system dialogs.
                var frame: Bitmap? = null
                scenario.onActivity { activity ->
                    val view = activity.window.decorView
                    frame = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
                        .also { view.draw(Canvas(it)) }
                }
                val image = checkNotNull(frame)
                assertTrue(image.width > 0 && image.height > 0)
                File(directory, "${if (dark) "dark" else "light"}-$index.png").outputStream().use {
                    image.compress(Bitmap.CompressFormat.PNG, 100, it)
                }
            }
        }
    }
}
