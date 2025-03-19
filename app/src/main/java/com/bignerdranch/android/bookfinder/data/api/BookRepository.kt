package com.bignerdranch.android.bookfinder.data.api

import com.bignerdranch.android.bookfinder.data.model.BookResponse
import retrofit2.Response

class BookRepository(private val apiService: BookApiService) {

    suspend fun searchBooks(query: String): Response<BookResponse> {
        return apiService.searchBook(query)
    }

}