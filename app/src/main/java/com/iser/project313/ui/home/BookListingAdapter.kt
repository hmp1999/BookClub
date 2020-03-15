package com.iser.project313.ui.home

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Base64
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
    private var onItemClickListener: AdapterView.OnItemClickListener,
    private var showMenu: Boolean = false
) : RecyclerView.Adapter<BookListingAdapter.BookListingViewHolder>() {
    private var dataSet = ArrayList<BookInfo>()

    init {
        dataSet.addAll(data)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookListingViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book_view, parent, false)
        return BookListingViewHolder(itemView, onItemClickListener, showMenu)
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
        val decodedString = Base64.decode(currentBook.albumCover, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        holder.bookIcon.setImageBitmap(decodedByte)
    }

    fun addItems(items: ArrayList<BookInfo>) {
        dataSet.addAll(items)
        notifyDataSetChanged()
    }

    fun getDataSet(): ArrayList<BookInfo> {
        return dataSet
    }

    fun clearData() {
        dataSet.clear()
        notifyDataSetChanged()
    }

    class BookListingViewHolder(
        item: View,
        onItemClickListener: AdapterView.OnItemClickListener,
        showMenu: Boolean
    ) :
        RecyclerView.ViewHolder(item) {
        var price: TextView = item.findViewById(R.id.tv_price)
        var title: TextView = item.findViewById(R.id.tv_title)
        var author: TextView = item.findViewById(R.id.tv_author)
        var bookIcon: ImageView = item.findViewById(R.id.img_bookIcon)
        var menuIcon: TextView = item.findViewById(R.id.tv_menu)

        init {
            if (showMenu)
                menuIcon.visibility = View.VISIBLE
            else menuIcon.visibility = View.INVISIBLE
            item.setOnClickListener {
                onItemClickListener.onItemClick(null, item, adapterPosition, itemId)
            }
            menuIcon.setOnClickListener {
                onItemClickListener.onItemClick(null, it, adapterPosition, itemId)
            }
        }
    }
}