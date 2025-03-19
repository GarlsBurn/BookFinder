package com.bignerdranch.android.bookfinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.bookfinder.data.api.BookRepository
import com.bignerdranch.android.bookfinder.data.model.BookItem
import kotlinx.coroutines.launch
import retrofit2.http.Query


class SearchViewModel(private val repository: BookRepository): ViewModel() {
    private val _books = MutableLiveData<List<BookItem>>()
    val books: LiveData<List<BookItem>> = _books

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun searchBook(query: String){
        if (query.isBlank()) return

        viewModelScope.launch {

            _loading.value = true
            try {
                val response = repository.searchBooks(query)

                if (response.isSuccessful){
                    val booksList = response.body()?.items ?: emptyList()
                    _books.value = booksList
                } else {
                    _errorMessage.value = "Ошибка выполнения запроса"
                }
            } catch (e: Exception){
                _errorMessage.value = "Ошибка сети"
            }
            _loading.value = false
        }

    }
}