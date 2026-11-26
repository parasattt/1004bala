package com.example.a1004bala.data.remote.dto

// Google Books omits optional metadata, including items for an empty search.
data class VolumesResponseDto(
    val totalItems: Int = 0,
    val items: List<VolumeDto>? = null
)

data class VolumeDto(
    val id: String? = null,
    val volumeInfo: VolumeInfoDto? = null
)

data class VolumeInfoDto(
    val title: String? = null,
    val authors: List<String>? = null,
    val pageCount: Int? = null,
    val publishedDate: String? = null,
    val imageLinks: ImageLinksDto? = null
)

data class ImageLinksDto(
    val thumbnail: String? = null,
    val smallThumbnail: String? = null
)
