package com.iser.project313.ui.orders

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.iser.project313.R
import com.iser.project313.ui.home.BookInfo
import com.iser.project313.ui.home.BookListingAdapter
import kotlinx.android.synthetic.main.activity_personal_listing.*


class PersonalListingActivity : AppCompatActivity() {

    private lateinit var personalListingAdapter: BookListingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_listing)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "My Books"
        setupUI()
        setupRecyclerView()
        getData()
    }


    private fun setupRecyclerView() {
        rv_your_books.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        personalListingAdapter = BookListingAdapter(ArrayList(),
            AdapterView.OnItemClickListener { adapterView, clickedView, index, p3 ->
                if (clickedView.id == R.id.tv_menu)
                    showMenu(clickedView, index)
            }, true
        )
        rv_your_books?.adapter = personalListingAdapter
    }

    private fun getData() {
        val dataSet: ArrayList<BookInfo> = java.util.ArrayList()
        val filteredData: ArrayList<BookInfo> = java.util.ArrayList()
        progress_circular?.visibility = View.VISIBLE
        val ref = FirebaseDatabase.getInstance().getReference("availableBooks")
        ref.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                progress_circular?.visibility = View.GONE
                swipeToRefresh_list?.isRefreshing = false
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.mapNotNullTo(dataSet) {
                    it.getValue<BookInfo>(BookInfo::class.java)
                }
                filteredData.addAll(dataSet.filter {
                    it.createdBy.equals(FirebaseAuth.getInstance().currentUser?.email)
                })
                progress_circular?.visibility = View.GONE
                swipeToRefresh_list?.isRefreshing = false
                personalListingAdapter.addItems(filteredData)
            }
        })
    }

    private fun setupUI() {
        swipeToRefresh_list?.setOnRefreshListener {
            personalListingAdapter.clearData()
            getData()
            swipeToRefresh_list?.isRefreshing = true
        }
    }

    private fun showMenu(view: View, index: Int) {
        //creating a popup menu
        val popup = PopupMenu(this, view)
        //inflating menu from xml resource
        popup.inflate(R.menu.item_menu)
        //adding click listener
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_delete -> {
                    deleteBook(index)
                    true
                }
                else -> {
                    false
                }
            }
        }
        //displaying the popup
        popup.show()
    }

    private fun deleteBook(index: Int) {
        val key = personalListingAdapter.getDataSet()[index].bookId
        FirebaseDatabase.getInstance().getReference("availableBooks/$key").removeValue()
            .addOnCompleteListener {
                Toast.makeText(this, "Book has been deleted successfully", Toast.LENGTH_LONG).show()
                personalListingAdapter.removeItem(index)
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
