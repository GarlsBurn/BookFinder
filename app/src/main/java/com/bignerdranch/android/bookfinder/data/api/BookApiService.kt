package com.bignerdranch.android.bookfinder.data.api

import com.bignerdranch.android.bookfinder.data.model.BookResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BookApiService {

    @GET("volume")
    suspend fun searchBook(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 20,
        @Query("orderBy") orderBy: String = "revalance"
    ):Response<BookResponse>

}