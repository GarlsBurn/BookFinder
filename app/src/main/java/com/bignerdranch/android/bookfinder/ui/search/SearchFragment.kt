package com.bignerdranch.android.bookfinder.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bignerdranch.android.bookfinder.R
import com.bignerdranch.android.bookfinder.adapter.BooksAdapter
import com.bignerdranch.android.bookfinder.data.api.BookApiService
import com.bignerdranch.android.bookfinder.data.api.BookRepository
import com.bignerdranch.android.bookfinder.data.api.RetrofitInstance
import com.bignerdranch.android.bookfinder.data.model.Book
import com.bignerdranch.android.bookfinder.data.model.BookResponse
import com.bignerdranch.android.bookfinder.viewmodel.SearchViewModel
import com.google.android.material.search.SearchBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.search_fragment) {

    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: BooksAdapter
    private val booksList = mutableListOf<Book>()

    private val bookRepository by lazy {
        BookRepository(RetrofitInstance.api)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.search_fragment, container, false)

        recyclerView = view.findViewById(R.id.recycle_view_search)
        progressBar = view.findViewById(R.id.progressBar)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = BooksAdapter(booksList) { book -> toggleFavorite(book) }
        recyclerView.adapter = adapter


        setHasOptionsMenu(true)

        loadFakeBooks()
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotEmpty()) {
                        fetchBooks(it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun fetchBooks(query: String) {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response = bookRepository.searchBooks(query)
                progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body()?.items != null) {
                    booksList.clear()
                    booksList.addAll(response.body()?.items?.map { item ->
                        Book(
                            id = item.id,
                            title = item.bookInfo.title,
                            author = item.bookInfo.author?.joinToString(", ") ?: "Неизвестный автор",
                            imageUrl = item.bookInfo.linkImages?.picture
                        )
                    } ?: emptyList())
                    adapter.notifyDataSetChanged()
                    recyclerView.visibility = View.VISIBLE
                } else {
                    showError("Ошибка загрузки данных")
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                showError("Ошибка подключения: ${e.message}")
            }
        }
    }

    private fun loadFakeBooks() {
        booksList.clear()
        booksList.addAll(List(10) { index ->
            Book(
                id = "fake_$index",
                title = "Книга $index",
                author = "Автор $index",
                imageUrl = "https://",
                isFavorite = false
            )
        })
        adapter.notifyDataSetChanged()
        progressBar.visibility = View.GONE
    }

    private fun toggleFavorite(book: Book) {
        book.isFavorite = !book.isFavorite
        adapter.notifyDataSetChanged()
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
