package com.example.a1004bala.data.remote

import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://www.googleapis.com/books/v1/"

    val api: GoogleBooksApi by lazy {
        createApi(BASE_URL)
    }

    // A configurable base URL also lets tests exercise real HTTP serialization locally.
    fun createApi(baseUrl: String): GoogleBooksApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(
            OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .callTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.NONE
                })
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GoogleBooksApi::class.java)
}
