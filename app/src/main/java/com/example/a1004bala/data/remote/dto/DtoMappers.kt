package com.example.a1004bala.data.remote.dto

import com.example.a1004bala.data.model.SearchBook
import java.io.IOException
import java.net.URI

fun VolumeDto.toSearchBook(): SearchBook {
    val volumeId = id?.trim()?.takeIf { it.isNotEmpty() }
        ?: throw IOException("Google Books returned a volume without an ID")
    val info = volumeInfo
    val authors = info?.authors.orEmpty().map { it.trim() }.filter { it.isNotEmpty() }
    return SearchBook(
        id = volumeId,
        title = info?.title?.trim()?.takeIf { it.isNotEmpty() } ?: "Атаусыз",
        author = authors.joinToString().ifEmpty { "Белгісіз автор" },
        pageCount = info?.pageCount?.takeIf { it > 0 },
        year = info?.publishedDate?.take(4)?.takeIf { it.length == 4 && it.all(Char::isDigit) },
        coverUrl = secureCoverUrl(info?.imageLinks?.thumbnail)
            ?: secureCoverUrl(info?.imageLinks?.smallThumbnail)
    )
}

private fun secureCoverUrl(value: String?): String? = runCatching {
    val uri = URI(value?.trim() ?: return null)
    if (uri.host.isNullOrBlank() || uri.userInfo != null) return null
    when (uri.scheme?.lowercase()) {
        "http" -> "https" + uri.toASCIIString().substring(4)
        "https" -> uri.toASCIIString()
        else -> null
    }
}.getOrNull()
