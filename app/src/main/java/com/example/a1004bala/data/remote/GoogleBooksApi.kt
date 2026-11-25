package com.example.a1004bala.data.remote

import com.example.a1004bala.data.remote.dto.VolumeDto
import com.example.a1004bala.data.remote.dto.VolumesResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GoogleBooksApi {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 20,
        @Query("printType") printType: String = "books"
    ): VolumesResponseDto

    @GET("volumes/{id}")
    suspend fun getVolume(@Path("id") id: String): VolumeDto
}
