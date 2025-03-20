package com.bignerdranch.android.bookfinder.data.model

import android.service.controls.templates.ThumbnailTemplate

data class Book(
    val title: String,
    val author: String,
    val imageUrl: String?,
    val id: String,
    var isFavorite: Boolean = false
)
