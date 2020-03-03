package com.iser.project313.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iser.project313.R

class BookListingAdapter(
    data: ArrayList<BookInfo>,
    private var onItemClickListener: AdapterView.OnItemClickListener
) : RecyclerView.Adapter<BookListingAdapter.BookListingViewHolder>() {
    private var dataSet = data
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookListingViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book_view, parent, false)
        return BookListingViewHolder(itemView, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BookListingViewHolder, position: Int) {
        val currentBook = dataSet[position]
        holder.title.text = currentBook.title
        holder.price.text = "$ " + currentBook.price
        holder.author.text = currentBook.author
        holder.bookIcon.setImageResource(currentBook.resourceId)
    }


    class BookListingViewHolder(item: View, onItemClickListener: AdapterView.OnItemClickListener) :
        RecyclerView.ViewHolder(item) {
        var price: TextView = item.findViewById(R.id.tv_price)
        var title: TextView = item.findViewById(R.id.tv_title)
        var author: TextView = item.findViewById(R.id.tv_author)
        var bookIcon: ImageView = item.findViewById(R.id.img_bookIcon)

        init {
            item.setOnClickListener {
                onItemClickListener.onItemClick(null, item, adapterPosition, itemId)
            }
        }
    }
}