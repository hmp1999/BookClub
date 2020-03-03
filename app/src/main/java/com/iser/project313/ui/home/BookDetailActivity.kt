package com.iser.project313.ui.home

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.iser.project313.R
import kotlinx.android.synthetic.main.activity_book_detail.*

class BookDetailActivity : AppCompatActivity() {
    private lateinit var bookDetail: BookInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)
        initData()
        initToolbar()
        setData()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        title = bookDetail.title
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_white)
    }

    private fun initData() {
        bookDetail = intent.getSerializableExtra("bookDetail") as BookInfo
    }

    private fun setData() {
        img_bookIcon.setImageResource(bookDetail.resourceId)
        tv_title.text = bookDetail.title
        tv_author.text = bookDetail.author
        tv_long_desc.text = bookDetail.longDesc
        tv_price.text = "$" + bookDetail.price
        book_rating.rating = bookDetail.rating
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
