package com.iser.project313.ui.cart

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iser.project313.R
import com.iser.project313.ui.CartDetail


class CartAdapter (
    data: ArrayList<Any>,
    private var onItemClickListener: AdapterView.OnItemClickListener,
    var showMenu: Boolean = false,
    var showRemoveIcon : Boolean = false
):RecyclerView.Adapter<CartAdapter.ItemViewHolder>() {
    private var dataSet = ArrayList<Any>()

    companion object {
        const val BOOKS = 0
        const val BILL = 1
        const val DEFAULT = 2
    }
    init {
        dataSet.addAll(data)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        when (viewType) {
            BOOKS -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_book_view, parent, false)
                return ItemViewHolder(view, viewType,onItemClickListener, showMenu, showRemoveIcon)
            }
           BILL -> {
               val view2: View = LayoutInflater.from(parent.context)
                   .inflate(R.layout.item_bill_info, parent, false)
               return ItemViewHolder(view2, viewType,onItemClickListener, showMenu, showRemoveIcon)
            }
            else -> {
                val defaultView: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_book_view, parent, false)
                return ItemViewHolder(defaultView, viewType,onItemClickListener, showMenu, showRemoveIcon)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        when(getItemViewType(position)){
            BOOKS -> {
                val currentBook = (dataSet[position] as CartDetail).productInfo
                holder.title?.text = currentBook.title
                holder.price?.text = "$ " + currentBook.price
                holder.author?.text = currentBook.author
                val decodedString = Base64.decode(currentBook.albumCover, Base64.DEFAULT)
                val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                holder.bookIcon?.setImageBitmap(decodedByte)
            }
            BILL -> {
                val currentBill = dataSet[position] as BillInfo
                val priceLabel = holder.itemView.context.getString(R.string.price).replaceFirst("#quantity", currentBill.quantity.toString(), false)
                holder.tvLabelPrice?.text = priceLabel
                val totalPrice = holder.itemView.context.getString(R.string.currency_price).replaceFirst("#currency", "$",false)
                    .replaceFirst("#price",currentBill.totalAmont.toString(), false)
                holder.tvPrice?.text = totalPrice
                if (currentBill.shippingCharge == 0.0)
                    holder.tvDeliveryCharges?.text = "Free"
                else holder.tvDeliveryCharges?.text = currentBill.shippingCharge.toString()
                holder.tvTotalAmount?.text = totalPrice
            }
        }
    }

    fun addCartItems(data : ArrayList<Any>){
        this.dataSet.clear()
        this.dataSet.addAll(data)
        notifyDataSetChanged()
    }

    fun getItems() : ArrayList<Any>{
        return this.dataSet
    }

    fun getItem(position: Int) : Any{
        return this.dataSet[position]
    }

    fun removeItem(position: Int){
        this.dataSet.remove(position)
        notifyItemRemoved(position)
    }

    override fun getItemViewType(position: Int): Int {
        if (dataSet[position] is CartDetail)
            return BOOKS
        if (dataSet[position] is BillInfo)
            return BILL
        return DEFAULT
    }



     class ItemViewHolder(item: View, itemViewType: Int,
                          onItemClickListener: AdapterView.OnItemClickListener,showMenu: Boolean, showRemoveIcon: Boolean) :
        RecyclerView.ViewHolder(item){

        var price: TextView? = item.findViewById(R.id.tv_price)
        var title: TextView? = item.findViewById(R.id.tv_title)
        var author: TextView? = item.findViewById(R.id.tv_author)
        var bookIcon: ImageView? = item.findViewById(R.id.img_bookIcon)
        var menuIcon: TextView? = item.findViewById(R.id.tv_menu)
        var tvLabelPrice : TextView? = item.findViewById(R.id.tv_label_price)
        var tvPrice : TextView? = item.findViewById(R.id.tv_price)
        var tvDeliveryCharges : TextView? = item.findViewById(R.id.tv_delivery_charges)
        var tvTotalAmount : TextView? = item.findViewById(R.id.tv_total_amount)
        var removeIcon : ImageButton? =  item.findViewById(R.id.btn_delete_from_cart)
        var divider : View? = item.findViewById(R.id.divider)

        init {
            when(itemViewType){
                BOOKS -> {
                    if (showMenu)
                        menuIcon?.visibility = View.VISIBLE
                    else menuIcon?.visibility = View.GONE

                    if (showRemoveIcon){
                        removeIcon?.visibility = View.VISIBLE
                        divider?.visibility = View.VISIBLE
                    }
                    else {
                        removeIcon?.visibility = View.GONE
                        divider?.visibility = View.GONE
                    }
                    item.setOnClickListener {
                        onItemClickListener.onItemClick(null, item, adapterPosition, itemId)
                    }
                    menuIcon?.setOnClickListener {
                        onItemClickListener.onItemClick(null, it, adapterPosition, itemId)
                    }
                    removeIcon?.setOnClickListener {
                        onItemClickListener.onItemClick(null, it, adapterPosition, itemId)
                    }
                }
            }
        }
    }
}