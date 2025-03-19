package com.bignerdranch.android.bookfinder.data.model

data class BookResponse(
    val items: List<BookItem>?
)

data class BookItem(
    val id: String,
    val bookInfo: BookInfo
)

data class BookInfo(
    val title: String,
    val author: List<String>?,
    val publishedDate: String?,
    val description: String?,
    val linkImages: LinkImages?
)

data class LinkImages(
    val picture: String?
)
