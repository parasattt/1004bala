package com.example.a1004bala.data.remote

import com.example.a1004bala.data.remote.dto.*
import com.example.a1004bala.data.repository.NetworkBooksRemoteRepository
import com.google.gson.Gson
import java.io.IOException
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class GoogleBooksRepositoryTest {
    @Test fun missingJsonFieldsBecomeSafeUiMetadata() {
        val dto = Gson().fromJson("""{"id":"abc","volumeInfo":{}}""", VolumeDto::class.java)
        val book = dto.toSearchBook()
        assertEquals("abc", book.id)
        assertEquals("Атаусыз", book.title)
        assertEquals("Белгісіз автор", book.author)
        assertNull(book.pageCount)
        assertNull(book.year)
        assertNull(book.coverUrl)
    }

    @Test fun mapsAuthorsYearAndSecureThumbnailFallback() {
        val book = VolumeDto("id", VolumeInfoDto(
            title = "  Абай жолы  ", authors = listOf("", " Мұхтар Әуезов ", "Other"),
            pageCount = 300, publishedDate = "2020-02-01",
            imageLinks = ImageLinksDto("invalid", "http://books.google.com/cover?id=x&zoom=1")
        )).toSearchBook()
        assertEquals("Абай жолы", book.title)
        assertEquals("Мұхтар Әуезов, Other", book.author)
        assertEquals(300, book.pageCount)
        assertEquals("2020", book.year)
        assertEquals("https://books.google.com/cover?id=x&zoom=1", book.coverUrl)
    }

    @Test fun rejectsInvalidMetadataAndUnsafeImageSchemes() {
        val book = VolumeDto("id", VolumeInfoDto(title = " ", authors = emptyList(),
            pageCount = -1, publishedDate = "n.d.",
            imageLinks = ImageLinksDto("file:///private/cover", "javascript:bad"))).toSearchBook()
        assertNull(book.pageCount)
        assertNull(book.year)
        assertNull(book.coverUrl)
    }

    @Test fun absentVolumeInfoIsSupportedButMissingIdIsRejected() {
        assertEquals("Атаусыз", VolumeDto("id").toSearchBook().title)
        assertThrows(IOException::class.java) { VolumeDto().toSearchBook() }
    }

    @Test fun emptyResponsesAndBlankQueryDoNotInventBooks() = runBlocking {
        val api = FakeApi()
        val repository = NetworkBooksRemoteRepository(api)
        assertTrue(repository.search("   ").isEmpty())
        assertNull(api.lastQuery)
        api.response = Gson().fromJson("""{"totalItems":0}""", VolumesResponseDto::class.java)
        assertTrue(repository.search("qwxzqwxz").isEmpty())
        assertEquals("qwxzqwxz", api.lastQuery)
    }

    @Test fun repositoryTrimsQueriesDeduplicatesAndSkipsUnusableIds() = runBlocking {
        val api = FakeApi().apply {
            response = VolumesResponseDto(items = listOf(VolumeDto(), VolumeDto("a"), VolumeDto("a")))
        }
        val books = NetworkBooksRemoteRepository(api).search("  абай  ")
        assertEquals("абай", api.lastQuery)
        assertEquals(listOf("a"), books.map { it.id })
    }

    @Test fun getsIndividualVolumeForAddBookPrefill() = runBlocking {
        val api = FakeApi()
        val book = NetworkBooksRemoteRepository(api).getBook("  selected  ")
        assertEquals("selected", api.lastId)
        assertEquals("selected", book.id)
    }

    @Test fun repositoryPropagatesNetworkFailureForViewModelHandling() {
        val repository = NetworkBooksRemoteRepository(FakeApi().apply { fail = true })
        assertThrows(IOException::class.java) { runBlocking { repository.search("абай") } }
    }

    private class FakeApi : GoogleBooksApi {
        var response = VolumesResponseDto()
        var lastQuery: String? = null
        var lastId: String? = null
        var fail = false
        override suspend fun searchBooks(query: String, maxResults: Int, printType: String): VolumesResponseDto {
            if (fail) throw IOException("offline")
            lastQuery = query
            return response
        }
        override suspend fun getVolume(id: String): VolumeDto {
            lastId = id
            return VolumeDto(id)
        }
    }
}
