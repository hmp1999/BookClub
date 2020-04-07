package com.iser.project313.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.iser.project313.R
import com.iser.project313.ui.cart.CartActivity
import com.iser.project313.ui.orders.PersonalListingActivity
import com.iser.project313.ui.user_info.CheckSessionActivity
import kotlinx.android.synthetic.main.activity_book_listing.*
import kotlinx.android.synthetic.main.book_listing_content.*
import kotlinx.android.synthetic.main.book_listing_main_content.*

class BookListingActivity : AppCompatActivity() {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bookListAdapter: BookListingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_listing)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawer_layout, R.string.app_name, R.string.app_name)
        actionBarDrawerToggle.syncState()
        drawer_layout.setDrawerListener(actionBarDrawerToggle)
        title = "Books"
        rv_book_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        bookListAdapter = BookListingAdapter(
            ArrayList(),
            AdapterView.OnItemClickListener { _, p1, p2, _ ->
                val intent = Intent(this, BookDetailActivity::class.java)
                intent.putExtra("bookDetailId", bookListAdapter.getDataSet()[p2].bookId)
                startActivity(intent)
            })
        rv_book_list.adapter = bookListAdapter
        val view = nav_view.getHeaderView(nav_view.headerCount - 1)
        view.findViewById<TextView>(R.id.tv_username).text =
            FirebaseAuth.getInstance().currentUser?.displayName
        view.findViewById<TextView>(R.id.tv_user_email).text =
            FirebaseAuth.getInstance().currentUser?.email
        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_signOut -> {
                    performSignOut()
                }
                R.id.nav_create_book -> {
                    openCreateBookActivity()
                    true
                }
                R.id.nav_your_books -> {
                    openYourListings()
                    true
                }
                else -> false
            }
        }
        swipeToRefresh?.setOnRefreshListener {
            bookListAdapter.clearData()
            getDataSet()

        }
        //   createBook()
        getDataSet()

    }

    private fun openYourListings() {
        startActivity(Intent(this, PersonalListingActivity::class.java))
    }

    private fun openCreateBookActivity() {
        startActivity(Intent(this, CreateBook::class.java))
    }


    private fun performSignOut(): Boolean {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(BookListingActivity@ this, CheckSessionActivity::class.java))
        finish()
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        actionBarDrawerToggle.syncState()
    }

    private fun getDataSet() {
        progressBar?.visibility = View.VISIBLE
        val dataSet: ArrayList<BookInfo> = java.util.ArrayList()
        FirebaseDatabase.getInstance().getReference("availableBooks")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    progressBar?.visibility = View.GONE
                    swipeToRefresh?.isRefreshing = false
                }

                override fun onDataChange(p0: DataSnapshot) {
                    p0.children.mapNotNullTo(dataSet) {
                        it.getValue<BookInfo>(BookInfo::class.java)
                    }
                    bookListAdapter.addItems(dataSet)
                    progressBar?.visibility = View.GONE
                    swipeToRefresh?.isRefreshing = false
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
            }
            R.id.item_cart ->{
                openCart()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        return true
    }

    private fun openCart(){
        startActivity(Intent(this, CartActivity::class.java))
    }
}
