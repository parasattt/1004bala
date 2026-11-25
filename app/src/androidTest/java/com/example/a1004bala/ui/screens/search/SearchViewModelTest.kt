package com.example.a1004bala.ui.screens.search

import androidx.lifecycle.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.a1004bala.data.model.SearchBook
import com.example.a1004bala.data.repository.BooksRemoteRepository
import java.io.IOException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.HttpException
import retrofit2.Response

@RunWith(AndroidJUnit4::class)
class SearchViewModelTest {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val store = ViewModelStore()
    private val repository = ControlledRepository()
    private lateinit var vm: SearchViewModel

    @Before fun setup() = onMain {
        val factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = SearchViewModel(repository) as T
        }
        vm = ViewModelProvider(store, factory)[SearchViewModel::class.java]
    }

    @After fun cleanup() = onMain { store.clear() }

    @Test fun blankQueryDoesNotCallNetwork() {
        onMain { vm.onQueryChange("  "); vm.search() }
        assertEquals(SearchUiState.Idle, state())
        assertTrue(repository.requests.isEmpty())
    }

    @Test fun loadingBecomesSuccessAndKeepsSelectedVolumeId() {
        onMain { vm.onQueryChange("  абай  "); vm.search() }
        assertEquals(SearchUiState.Loading, state())
        assertEquals(listOf("абай"), repository.requests)
        onMain { repository.gates[0].complete(listOf(book("selected"))) }
        assertEquals("selected", (state() as SearchUiState.Success).books.single().id)
    }

    @Test fun noResultsBecomesEmptyWithSubmittedQuery() {
        onMain { vm.onQueryChange("qwxzqwxz"); vm.search(); repository.gates[0].complete(emptyList()) }
        assertEquals(SearchUiState.Empty("qwxzqwxz"), state())
    }

    @Test fun networkFailureAndRetryUseSameQuery() {
        onMain { vm.onQueryChange("абай"); vm.search(); repository.gates[0].completeExceptionally(IOException("offline")) }
        assertTrue(state() is SearchUiState.Error)
        onMain { vm.retry() }
        assertEquals(SearchUiState.Loading, state())
        assertEquals(listOf("абай", "абай"), repository.requests)
        onMain { repository.gates[1].complete(listOf(book("retried"))) }
        assertEquals("retried", (state() as SearchUiState.Success).books.single().id)
    }

    @Test fun serverFailureBecomesError() {
        onMain { vm.onQueryChange("абай"); vm.search()
            repository.gates[0].completeExceptionally(HttpException(Response.error<Unit>(503, "down".toResponseBody()))) }
        assertTrue((state() as SearchUiState.Error).message.contains("503"))
    }

    @Test fun olderNonCancellableRequestCannotReplaceNewerResults() {
        repository.ignoreCancellation = true
        onMain { vm.onQueryChange("old"); vm.search(); vm.onQueryChange("new"); vm.search() }
        onMain { repository.gates[1].complete(listOf(book("new"))) }
        assertEquals("new", (state() as SearchUiState.Success).books.single().id)
        onMain { repository.gates[0].complete(listOf(book("old"))) }
        assertEquals("new", (state() as SearchUiState.Success).books.single().id)
    }

    @Test fun clearCancelsLoadingAndDoesNotBecomeError() {
        onMain { vm.onQueryChange("абай"); vm.search(); vm.clear() }
        assertEquals("", onMain { vm.query.value })
        assertEquals(SearchUiState.Idle, state())
    }

    private fun state(): SearchUiState {
        instrumentation.waitForIdleSync()
        return onMain { vm.uiState.value }
    }
    private fun <T> onMain(action: () -> T): T {
        var result: Result<T>? = null
        instrumentation.runOnMainSync { result = runCatching(action) }
        return checkNotNull(result).getOrThrow()
    }
    private fun book(id: String) = SearchBook(id, "Абай", "Автор", 300, "2020", null)

    private class ControlledRepository : BooksRemoteRepository {
        val requests = mutableListOf<String>()
        val gates = mutableListOf<CompletableDeferred<List<SearchBook>>>()
        var ignoreCancellation = false
        override suspend fun search(query: String): List<SearchBook> {
            requests += query
            val gate = CompletableDeferred<List<SearchBook>>().also { gates += it }
            return if (ignoreCancellation) withContext(NonCancellable) { gate.await() } else gate.await()
        }
        override suspend fun getBook(volumeId: String): SearchBook = error("not used by search")
    }
}
