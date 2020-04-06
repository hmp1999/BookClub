package com.iser.project313.ui.cart

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
import com.iser.project313.ui.home.BookInfo


class CartAdapter (
    data: ArrayList<Any>,
    private var onItemClickListener: AdapterView.OnItemClickListener,
    var showMenu: Boolean = false
):RecyclerView.Adapter<CartAdapter.ItemViewHolder>() {
    private var dataSet = ArrayList<Any>()
    private val BOOKS = 0
    private val BILL = 1
    private val DEFAULT = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        when (viewType) {
            BOOKS -> {
                val view: View
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_book_view, parent, false)
                return ItemViewHolder(view, viewType,onItemClickListener, showMenu)
            }
           BILL -> {
                val view2: View
                view2 = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_book_view, parent, false)
               return ItemViewHolder(view2, viewType,onItemClickListener, showMenu)
            }
            else -> {
                val defaultView: View
                defaultView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_book_view, parent, false)
                return ItemViewHolder(defaultView, viewType,onItemClickListener, showMenu)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        when(getItemViewType(position)){
            BOOKS -> {
                val currentBook = dataSet[position] as BookInfo
                holder.title.text = currentBook.title
                holder.price.text = "$ " + currentBook.price
                holder.author.text = currentBook.author
                val decodedString = Base64.decode(currentBook.albumCover, Base64.DEFAULT)
                val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                holder.bookIcon.setImageBitmap(decodedByte)
            }
            BILL -> {
                val currentBill = dataSet[position] as BillInfo
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (dataSet.get(position) is BookInfo)
            return BOOKS
        if (dataSet.get(position) is BillInfo)
            return BILL
        return DEFAULT
    }



     class ItemViewHolder(item: View, itemViewType: Int,
                          onItemClickListener: AdapterView.OnItemClickListener,showMenu: Boolean) :
        RecyclerView.ViewHolder(item){

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