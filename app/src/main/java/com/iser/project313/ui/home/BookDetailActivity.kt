package com.iser.project313.ui.home

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.iser.project313.R
import kotlinx.android.synthetic.main.activity_book_detail.*

class BookDetailActivity : AppCompatActivity() {
    private var bookDetail: BookInfo? = null
    private lateinit var bookId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)
        initViews()
        initData()
        initToolbar()
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

    private fun initViews() {
        btn_add_to_bag?.setOnClickListener {
            //    addItemToBag()
        }
    }

    private fun itemExist() {
        FirebaseDatabase.getInstance().getReference("/orders/$bookId")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {

                }

            })
    }

    private fun addItemToBag() {
        val ordersRef = FirebaseDatabase.getInstance().getReference("/orders")
        val newOrder = ordersRef.push()
        val key = newOrder.key

    }
}
