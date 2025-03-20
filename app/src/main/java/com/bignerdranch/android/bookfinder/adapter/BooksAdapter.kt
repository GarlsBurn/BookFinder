package com.bignerdranch.android.bookfinder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.bookfinder.R
import com.bignerdranch.android.bookfinder.data.model.Book
import com.bumptech.glide.Glide

class BooksAdapter(
    private val books: List<Book>,
    private val onFavoriteClick: (Book) -> Unit
) : RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view, onFavoriteClick)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount(): Int = books.size

    class BookViewHolder(itemView: View, private val onFavoriteClick: (Book) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val bookTitle: TextView = itemView.findViewById(R.id.bookTitle)
        private val authorTitle: TextView = itemView.findViewById(R.id.authorTitle)
        private val bookImage: ImageView = itemView.findViewById(R.id.bookImage)
        private val favoriteButton: ImageView = itemView.findViewById(R.id.favoriteButton)

        fun bind(book: Book) {
            bookTitle.text = book.title
            authorTitle.text = book.author

            if (!book.imageUrl.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(book.imageUrl)
                    .into(bookImage)
            } else {
                bookImage.setImageResource(R.drawable.expl)
            }


            favoriteButton.setOnClickListener {
                onFavoriteClick(book)
            }
        }
    }
}
