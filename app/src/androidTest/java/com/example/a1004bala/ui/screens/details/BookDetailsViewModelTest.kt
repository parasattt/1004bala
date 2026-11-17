package com.example.a1004bala.ui.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.a1004bala.data.model.Book
import com.example.a1004bala.data.model.BookStatus
import com.example.a1004bala.data.model.Quote
import com.example.a1004bala.data.repository.BookRepository
import com.example.a1004bala.data.repository.InMemoryBookRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookDetailsViewModelTest {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val stores = mutableListOf<ViewModelStore>()
    private lateinit var repository: ControlledRepository
    private lateinit var handle: SavedStateHandle
    private lateinit var viewModel: BookDetailsViewModel

    @Before
    fun setUp() = onMain {
        repository = ControlledRepository()
        handle = SavedStateHandle(mapOf("bookId" to 1))
        viewModel = createViewModel(handle)
    }

    @After
    fun tearDown() = onMain {
        stores.forEach { it.clear() }
        stores.clear()
    }

    @Test
    fun loadsBookAndItsQuotesFromNavigationId() {
        val state = state()
        assertFalse(state.isLoading)
        assertFalse(state.isQuotesLoading)
        assertEquals(1, state.book?.id)
        assertEquals("120", state.pageInput)
        assertEquals(2, state.quotes.size)
        assertTrue(state.quotes.all { it.bookId == 1 })
    }

    @Test
    fun missingBookOrArgumentDoesNotStayLoading() {
        val missing = onMain { createViewModel(SavedStateHandle(mapOf("bookId" to -1))) }
        assertNull(state(missing).book)
        assertFalse(state(missing).isLoading)
        val noArgument = onMain { createViewModel(SavedStateHandle()) }
        assertFalse(state(noArgument).isLoading)
        assertFalse(state(noArgument).isQuotesLoading)
    }

    @Test
    fun reportsBookAndQuoteLoadFailuresIndependently() {
        val bookFailure = onMain {
            createViewModel(handle, ControlledRepository().apply { failBookLoad = true })
        }
        assertNotNull(state(bookFailure).actionError)
        assertFalse(state(bookFailure).isLoading)
        val quoteFailure = onMain {
            createViewModel(handle, ControlledRepository().apply { failQuotesLoad = true })
        }
        assertNotNull(state(quoteFailure).book)
        assertNotNull(state(quoteFailure).quotesLoadError)
        assertFalse(state(quoteFailure).isQuotesLoading)
    }

    @Test
    fun editingAndSteppingOnlyChangeDraft() {
        onMain {
            viewModel.onPageInputChange("121")
            viewModel.stepPage(1)
        }
        assertEquals("122", state().pageInput)
        assertEquals(120, state().book?.currentPage)
        assertEquals(0, calls(Operation.PAGES))
    }

    @Test
    fun stepButtonsRespectBoundsAndRejectInvalidDrafts() {
        onMain { viewModel.onPageInputChange("0"); viewModel.stepPage(-1) }
        assertEquals("0", state().pageInput)
        onMain { viewModel.onPageInputChange("540"); viewModel.stepPage(1) }
        assertEquals("540", state().pageInput)
        onMain { viewModel.onPageInputChange("abc"); viewModel.stepPage(1) }
        assertEquals("abc", state().pageInput)
        onMain { viewModel.onPageInputChange("120"); viewModel.stepPage(100) }
        assertEquals("120", state().pageInput)
    }

    @Test
    fun savesPagesAndUpdatesProgressFromRepository() {
        onMain { viewModel.onPageInputChange(" 270 "); viewModel.updatePages() }
        val state = state()
        assertEquals(270, state.book?.currentPage)
        assertEquals(50, state.book?.progressPercent)
        assertEquals("270", state.pageInput)
        assertFalse(state.isSaving)
        val restored = onMain { createViewModel(snapshot(handle)) }
        assertEquals("270", state(restored).pageInput)
    }

    @Test
    fun invalidPageInputNeverReachesRepository() {
        listOf("", "-1", "541", "1.5", "99999999999999999999").forEach { input ->
            onMain { viewModel.onPageInputChange(input); viewModel.updatePages() }
            assertNotNull(state().pageError)
        }
        assertEquals(0, calls(Operation.PAGES))
        assertEquals(120, state().book?.currentPage)
    }

    @Test
    fun pageSaveFailureKeepsDraftAndAllowsRetry() {
        onMain {
            repository.failOperation = Operation.PAGES
            viewModel.onPageInputChange("270")
            viewModel.updatePages()
        }
        assertNotNull(state().actionError)
        assertEquals("270", state().pageInput)
        assertEquals(120, state().book?.currentPage)
        assertFalse(state().isSaving)
        onMain { repository.failOperation = null; viewModel.updatePages() }
        assertEquals(270, state().book?.currentPage)
        assertNull(state().actionError)
    }

    @Test
    fun pendingPageSaveBlocksDuplicateWritesAndDraftChanges() {
        onMain {
            repository.gatedOperation = Operation.PAGES
            viewModel.onPageInputChange("540")
            viewModel.updatePages()
            viewModel.updatePages()
            viewModel.onPageInputChange("17")
        }
        assertTrue(state().isSaving)
        assertEquals("540", state().pageInput)
        assertEquals(1, calls(Operation.PAGES))
        releaseGate()
        assertEquals(BookStatus.FINISHED, state().book?.status)
        assertEquals(100, state().book?.progressPercent)
        assertFalse(state().isSaving)
    }

    @Test
    fun addsTrimmedQuoteAndClearsSavedDrafts() {
        onMain {
            viewModel.onQuoteInputChange("  Жаңа дәйексөз  ")
            viewModel.onQuotePageInputChange(" 12 ")
            viewModel.addQuote()
        }
        val added = state().quotes.single { it.text == "Жаңа дәйексөз" }
        assertEquals(1, added.bookId)
        assertEquals(12, added.page)
        assertEquals(3, state().quotes.size)
        assertEquals("", state().quoteInput)
        assertEquals("", state().quotePageInput)
        val restored = onMain { createViewModel(snapshot(handle)) }
        assertEquals("", state(restored).quoteInput)
        assertEquals("", state(restored).quotePageInput)
    }

    @Test
    fun validatesQuoteAndAcceptsUnspecifiedPage() {
        onMain { viewModel.addQuote() }
        assertNotNull(state().quoteError)
        onMain {
            viewModel.onQuoteInputChange("Дәйексөз")
            viewModel.onQuotePageInputChange("0")
            viewModel.addQuote()
        }
        assertNotNull(state().quotePageError)
        assertEquals(0, calls(Operation.ADD))
        onMain { viewModel.onQuotePageInputChange(""); viewModel.addQuote() }
        assertNull(state().quotes.single { it.text == "Дәйексөз" }.page)
        assertEquals(1, calls(Operation.ADD))
    }

    @Test
    fun quoteSaveFailurePreservesInputAndAllowsRetry() {
        onMain {
            repository.failOperation = Operation.ADD
            viewModel.onQuoteInputChange("Дәйексөз")
            viewModel.onQuotePageInputChange("12")
            viewModel.addQuote()
        }
        assertNotNull(state().quoteSaveError)
        assertEquals("Дәйексөз", state().quoteInput)
        assertEquals("12", state().quotePageInput)
        assertFalse(state().isAddingQuote)
        assertEquals(2, state().quotes.size)
        onMain { repository.failOperation = null; viewModel.addQuote() }
        assertEquals(3, state().quotes.size)
        assertNull(state().quoteSaveError)
    }

    @Test
    fun pendingQuoteSaveBlocksDuplicateWrites() {
        onMain {
            repository.gatedOperation = Operation.ADD
            viewModel.onQuoteInputChange("Дәйексөз")
            viewModel.addQuote()
            viewModel.addQuote()
            viewModel.onQuoteInputChange("Басқа мәтін")
        }
        assertTrue(state().isAddingQuote)
        assertEquals("Дәйексөз", state().quoteInput)
        assertEquals(1, calls(Operation.ADD))
        releaseGate()
        assertEquals(3, state().quotes.size)
        assertFalse(state().isAddingQuote)
    }

    @Test
    fun deletesSelectedQuoteAndRejectsUnknownId() {
        onMain { viewModel.deleteQuote(999) }
        assertEquals(0, calls(Operation.DELETE))
        onMain { viewModel.deleteQuote(1) }
        assertEquals(1, state().quotes.size)
        assertTrue(state().quotes.none { it.id == 1 })
        assertTrue(state().deletingQuoteIds.isEmpty())
    }

    @Test
    fun failedDeletionKeepsQuoteAndAllowsRetry() {
        onMain { repository.failOperation = Operation.DELETE; viewModel.deleteQuote(1) }
        assertEquals(2, state().quotes.size)
        assertNotNull(state().quoteDeleteError)
        assertTrue(state().deletingQuoteIds.isEmpty())
        onMain { repository.failOperation = null; viewModel.deleteQuote(1) }
        assertEquals(1, state().quotes.size)
        assertNull(state().quoteDeleteError)
    }

    @Test
    fun pendingDeletionBlocksDuplicateRequests() {
        onMain {
            repository.gatedOperation = Operation.DELETE
            viewModel.deleteQuote(1)
            viewModel.deleteQuote(1)
        }
        assertTrue(1 in state().deletingQuoteIds)
        assertEquals(1, calls(Operation.DELETE))
        releaseGate()
        assertEquals(1, state().quotes.size)
        assertTrue(state().deletingQuoteIds.isEmpty())
    }

    @Test
    fun cancellingStatusChoiceDoesNotWrite() {
        onMain {
            viewModel.openStatusSheet()
            viewModel.selectStatus(BookStatus.PLANNED)
        }
        assertEquals(BookStatus.READING, state().book?.status)
        onMain { viewModel.dismissStatusSheet(); viewModel.openStatusSheet() }
        assertEquals(BookStatus.READING, state().selectedStatus)
        onMain { viewModel.changeStatus() }
        assertEquals(0, calls(Operation.STATUS))
        assertFalse(state().isStatusSheetOpen)
    }

    @Test
    fun statusFailureKeepsSheetOpenAndRetryFinishesBook() {
        onMain {
            repository.failOperation = Operation.STATUS
            viewModel.onPageInputChange("200")
            viewModel.openStatusSheet()
            viewModel.selectStatus(BookStatus.FINISHED)
            viewModel.changeStatus()
        }
        assertTrue(state().isStatusSheetOpen)
        assertNotNull(state().statusError)
        assertFalse(state().isChangingStatus)
        assertEquals(BookStatus.READING, state().book?.status)
        onMain { repository.failOperation = null; viewModel.changeStatus() }
        assertFalse(state().isStatusSheetOpen)
        assertEquals(BookStatus.FINISHED, state().book?.status)
        assertEquals("540", state().pageInput)
        val restored = onMain { createViewModel(snapshot(handle)) }
        assertEquals("540", state(restored).pageInput)
    }

    @Test
    fun pendingStatusChangeBlocksDuplicatesPageWritesAndDismissal() {
        onMain {
            repository.gatedOperation = Operation.STATUS
            viewModel.openStatusSheet()
            viewModel.selectStatus(BookStatus.FINISHED)
            viewModel.changeStatus()
            viewModel.changeStatus()
            viewModel.onPageInputChange("20")
            viewModel.updatePages()
            viewModel.dismissStatusSheet()
        }
        assertTrue(state().isStatusSheetOpen)
        assertTrue(state().isChangingStatus)
        assertEquals(1, calls(Operation.STATUS))
        assertEquals(0, calls(Operation.PAGES))
        assertEquals("120", state().pageInput)
        releaseGate()
        assertFalse(state().isChangingStatus)
        assertFalse(state().isStatusSheetOpen)
        assertEquals("540", state().pageInput)
    }

    @Test
    fun restoredBlankDraftSurvivesBookUpdates() {
        onMain {
            viewModel.onPageInputChange("")
            viewModel.onQuoteInputChange("Черновик")
        }
        val restored = onMain { createViewModel(snapshot(handle)) }
        assertEquals("", state(restored).pageInput)
        assertEquals("Черновик", state(restored).quoteInput)
        onMain { viewModel.openStatusSheet(); viewModel.selectStatus(BookStatus.FINISHED); viewModel.changeStatus() }
        assertEquals(540, state(restored).book?.currentPage)
        assertEquals("", state(restored).pageInput)
    }

    private fun state(model: BookDetailsViewModel = viewModel): BookDetailsUiState {
        instrumentation.waitForIdleSync()
        return onMain { model.uiState.value }
    }

    private fun calls(operation: Operation) = onMain { repository.calls[operation] ?: 0 }

    private fun releaseGate() = onMain { repository.gate.complete(Unit); Unit }

    private fun snapshot(source: SavedStateHandle) =
        SavedStateHandle(source.keys().associateWith { source.get<Any?>(it) })

    private fun createViewModel(
        stateHandle: SavedStateHandle,
        source: BookRepository = repository
    ): BookDetailsViewModel {
        val store = ViewModelStore().also { stores += it }
        val factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                BookDetailsViewModel(stateHandle, source) as T
        }
        return ViewModelProvider(store, factory)[BookDetailsViewModel::class.java]
    }

    private fun <T> onMain(action: () -> T): T {
        var result: Result<T>? = null
        instrumentation.runOnMainSync { result = runCatching(action) }
        return checkNotNull(result).getOrThrow()
    }

    private enum class Operation { PAGES, ADD, DELETE, STATUS }

    private class ControlledRepository(
        private val delegate: BookRepository = InMemoryBookRepository()
    ) : BookRepository by delegate {
        val calls = mutableMapOf<Operation, Int>()
        var failOperation: Operation? = null
        var gatedOperation: Operation? = null
        var failBookLoad = false
        var failQuotesLoad = false
        val gate = CompletableDeferred<Unit>()

        override fun getBook(id: Int): Flow<Book?> =
            if (failBookLoad) flow { error("Simulated book load failure") } else delegate.getBook(id)

        override fun getQuotes(bookId: Int): Flow<List<Quote>> =
            if (failQuotesLoad) flow { error("Simulated quote load failure") } else delegate.getQuotes(bookId)

        override suspend fun updateCurrentPage(bookId: Int, currentPage: Int) {
            before(Operation.PAGES)
            delegate.updateCurrentPage(bookId, currentPage)
        }

        override suspend fun addQuote(bookId: Int, text: String, page: Int?) {
            before(Operation.ADD)
            delegate.addQuote(bookId, text, page)
        }

        override suspend fun deleteQuote(quoteId: Int) {
            before(Operation.DELETE)
            delegate.deleteQuote(quoteId)
        }

        override suspend fun updateStatus(bookId: Int, status: BookStatus) {
            before(Operation.STATUS)
            delegate.updateStatus(bookId, status)
        }

        private suspend fun before(operation: Operation) {
            calls[operation] = (calls[operation] ?: 0) + 1
            check(failOperation != operation) { "Simulated write failure" }
            if (gatedOperation == operation) gate.await()
        }
    }
}
