package com.iser.project313.ui.wishlist

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.iser.project313.R
import com.iser.project313.ui.BaseActivity
import com.iser.project313.ui.CartDetail
import com.iser.project313.ui.cart.CartAdapter
import kotlinx.android.synthetic.main.activity_book_detail.*
import kotlinx.android.synthetic.main.activity_my_wish_list.*
import kotlinx.android.synthetic.main.activity_my_wish_list.toolbar
import kotlinx.android.synthetic.main.book_listing_main_content.*


class MyWishListActivity : BaseActivity() {
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_wish_list)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        title = ""
        initViews()
        getWishlistDetails()
    }

    private fun getWishlistDetails() {
        showProgressDialog()
        val data = ArrayList<Any>()
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        FirebaseDatabase.getInstance().getReference("/wishlist").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Snackbar.make(
                    btn_add_to_bag,
                    "Something went wrong",
                    Snackbar.LENGTH_LONG
                ).show()
                hideProgressDialog()
            }

            override fun onDataChange(dataSet: DataSnapshot) {
                dataSet.children.mapNotNullTo(data){
                    it.getValue<CartDetail>(CartDetail::class.java)
                }
                data.filter {
                    it as CartDetail
                    it.createdBy.equals(userEmail)
                }
                adapter.addCartItems(data)
                btn_clear_all_wishlist?.isEnabled = true
                if (data.size > 0){
                    btn_clear_all_wishlist?.visibility = View.VISIBLE
                    img_no_data?.visibility = View.GONE
                }
                else{
                    btn_clear_all_wishlist?.visibility = View.GONE
                    img_no_data?.visibility = View.VISIBLE
                }
                hideProgressDialog()
            }
        })
    }

    private fun initViews() {
        rv_book_list?.setPadding(0,0,0,200)
        rv_book_list?.clipToPadding = false
        adapter = CartAdapter(ArrayList(), AdapterView.OnItemClickListener { parent, clickedView, position, id ->
            if (clickedView.id == R.id.btn_delete_from_cart)
                deleteWishListItem(position, adapter.getItem(position) as CartDetail)
        },showRemoveIcon = true)
        rv_book_list?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        rv_book_list?.adapter = adapter
        btn_clear_all_wishlist?.setOnClickListener {
            deleteAllWishlist()
        }
        swipeToRefresh?.setOnRefreshListener {
            getWishlistDetails()
            swipeToRefresh?.isRefreshing= false
        }
    }

    private fun deleteAllWishlist() {
        showProgressDialog()
        val dataSet = adapter.getItems()
        var key : String? = null
        dataSet.forEach { item ->
            item as CartDetail
            key = item.cartId
            FirebaseDatabase.getInstance().getReference("wishlist/$key").removeValue()
        }
        Snackbar.make(rv_book_list, "Items has been removed from wishlist", Snackbar.LENGTH_LONG)
            .show()
        hideProgressDialog()
        getWishlistDetails()
    }

    private fun deleteWishListItem(position : Int ,item: CartDetail) {
        showProgressDialog()
        val key = item.cartId
        FirebaseDatabase.getInstance().getReference("wishlist/$key").removeValue()
            .addOnCompleteListener {
                Snackbar.make(rv_book_list, "Item has been removed from wishlist", Snackbar.LENGTH_LONG)
                    .show()
                adapter.removeItem(position)
                hideProgressDialog()
                getWishlistDetails()
            }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
