package com.iser.project313.ui.home

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.iser.project313.R
import com.iser.project313.ui.BaseActivity
import com.iser.project313.ui.CartDetail
import com.iser.project313.ui.cart.CartActivity
import kotlinx.android.synthetic.main.activity_book_detail.*

class BookDetailActivity : BaseActivity() {
    private var bookDetail: BookInfo? = null
    private lateinit var bookId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)
        showProgressDialog()
        initViews()
        initData()
        initToolbar()
        hideProgressDialog()
        itemExist()
        itemExistToWishList()

    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_white)
    }

    private fun initData() {
        bookId = intent.getStringExtra("bookDetailId")
        getData()
    }

    private fun getData() {
        FirebaseDatabase.getInstance().getReference("availableBooks/$bookId")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    bookDetail = dataSnapshot.getValue<BookInfo>(BookInfo::class.java)
                    setData()
                }

            })
    }

    private fun setData() {
        val decodedString = Base64.decode(bookDetail?.albumCover, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        img_bookIcon?.setImageBitmap(decodedByte)
        tv_title.text = bookDetail?.title
        tv_author.text = bookDetail?.author
        tv_long_desc.text = bookDetail?.longDesc
        tv_price.text = "$" + bookDetail?.price
        book_rating.rating = bookDetail?.rating ?: 0.0F
        title = bookDetail?.title
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews(){
        btn_add_to_bag?.setOnClickListener {
            addItemToBag()
        }
        btn_add_to_wishList?.setOnClickListener {
            addToWishList()
        }
    }

    private fun addToWishList() {
        showProgressDialog()
        val cartRef = FirebaseDatabase.getInstance().getReference("/wishlist")
        val newBag = cartRef.push()
        val key = newBag.key
        val cart = CartDetail(key!!)
        cart.productId = bookId
        cart.productInfo = bookDetail!!
        if (FirebaseAuth.getInstance().currentUser!=null)
            cart.createdBy = FirebaseAuth.getInstance().currentUser?.email
        newBag.setValue(cart){ databaseError, _ ->
            if (databaseError == null) {
                Snackbar.make(
                    btn_add_to_bag,
                    "Item added to wishlist",
                    Snackbar.LENGTH_LONG
                ).show()
                hideWishlistButton(true)
            }
            else{
                Snackbar.make(btn_add_to_bag,"Failed to add to wishlist",Snackbar.LENGTH_LONG).show()
                hideWishlistButton(false)
            }
            hideProgressDialog()
        }
    }

    private fun itemExist(){
        showProgressDialog()
        var data = ArrayList<CartDetail>()
        var itemExist : Boolean = false
        var userEmail = FirebaseAuth.getInstance().currentUser?.email
        FirebaseDatabase.getInstance().getReference("/bag").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Snackbar.make(
                    btn_add_to_bag,
                    "Something went wrong",
                    Snackbar.LENGTH_LONG
                ).show()
            }

            override fun onDataChange(dataSet: DataSnapshot) {
                dataSet.children.mapNotNullTo(data){
                    it.getValue<CartDetail>(CartDetail::class.java)
                }
                data.forEach {
                    if (it.createdBy!=null && it.createdBy.equals(userEmail) && it.productInfo.bookId.equals(bookId)){
                        itemExist = true
                    }
                }
                hideCartButton(itemExist)
                hideProgressDialog()
            }
        })
    }

    private fun hideCartButton(hide: Boolean) {
        if (hide){
            btn_add_to_bag?.visibility = View.GONE
            btn_order_now?.visibility = View.VISIBLE
        }else {
            btn_add_to_bag?.visibility = View.VISIBLE
            btn_order_now?.visibility = View.GONE
        }
    }

    private fun hideWishlistButton(hide: Boolean) {
        if (hide){
            btn_add_to_wishList?.visibility = View.GONE
            btn_order_now?.visibility = View.VISIBLE
            btn_add_to_bag?.setBackgroundColor(Color.WHITE)
            btn_add_to_bag?.setTextColor(ResourcesCompat.getColor(resources, R.color.colorAccent, null))
        }else {
            btn_add_to_wishList?.visibility = View.VISIBLE
            btn_order_now?.visibility = View.GONE
            btn_add_to_bag?.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.colorAccent, null))
            btn_add_to_bag?.setTextColor(Color.WHITE)
        }
    }

    private fun addItemToBag(){
        showProgressDialog()
        val cartRef = FirebaseDatabase.getInstance().getReference("/bag")
        val newBag = cartRef.push()
        val key = newBag.key
        val cart = CartDetail(key!!)
        cart.productId = bookId
        cart.productInfo = bookDetail!!
        if (FirebaseAuth.getInstance().currentUser!=null)
            cart.createdBy = FirebaseAuth.getInstance().currentUser?.email
        newBag.setValue(cart){ databaseError, _ ->
            if (databaseError == null) {
                Snackbar.make(
                    btn_add_to_bag,
                    "Item added to cart successfully",
                    Snackbar.LENGTH_LONG
                ).show()
                hideCartButton(true)
            }
            else {
                Snackbar.make(btn_add_to_bag,"Failed to add to cart",Snackbar.LENGTH_LONG).show()
                hideCartButton(false)
            }

            hideProgressDialog()
        }
    }

    private fun itemExistToWishList(){
        var data = ArrayList<CartDetail>()
        var itemExist : Boolean = false
        var userEmail = FirebaseAuth.getInstance().currentUser?.email
        FirebaseDatabase.getInstance().getReference("/wishlist").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Snackbar.make(
                    btn_add_to_bag,
                    "Something went wrong",
                    Snackbar.LENGTH_LONG
                ).show()
            }

            override fun onDataChange(dataSet: DataSnapshot) {
                dataSet.children.mapNotNullTo(data){
                    it.getValue<CartDetail>(CartDetail::class.java)
                }
                data.forEach {
                    if (it.createdBy!=null && it.createdBy.equals(userEmail) && it.productInfo.bookId.equals(bookId)){
                        itemExist = true
                    }
                }
                hideWishlistButton(itemExist)
                hideProgressDialog()
            }
        })
    }


}
