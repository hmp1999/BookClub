package com.iser.project313.ui.orders

import android.os.Bundle
import android.view.MenuItem
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
import kotlinx.android.synthetic.main.activity_my_orders.toolbar
import kotlinx.android.synthetic.main.book_listing_main_content.*

class MyOrdersActivity : BaseActivity() {

    private lateinit var adapter: CartAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_orders)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        title = "My Orders"
        initViews()
        getOrderDetails()
    }

    private fun initViews() {
        rv_book_list?.setPadding(0,0,0,200)
        rv_book_list?.clipToPadding = false
        adapter = CartAdapter(ArrayList(), AdapterView.OnItemClickListener { parent, clickedView, position, id ->
        },showRemoveIcon = true)
        rv_book_list?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        rv_book_list?.adapter = adapter
        swipeToRefresh?.setOnRefreshListener {
            getOrderDetails()
            swipeToRefresh?.isRefreshing= false
        }
    }

    private fun getOrderDetails() {
        showProgressDialog()
        val data = ArrayList<OrderDetails>()
        val finalData : ArrayList<Any> = ArrayList()
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        FirebaseDatabase.getInstance().getReference("/orders").addListenerForSingleValueEvent(object :
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
                    it.getValue<OrderDetails>(OrderDetails::class.java)
                }
                data.filter {
                    it as OrderDetails
                    it.deliveredTo.equals(userEmail)
                }
                data.forEach { order->
                    order.products.forEach {cart ->
                        val neworder  = OrderDetails(order.orderId)
                        neworder.orderTitle = cart.productInfo.title
                        neworder.orderedOn = order.orderedOn
                        val products : ArrayList<CartDetail> = ArrayList()
                        products.add(cart)
                        neworder.products = products
                        finalData.add(neworder)
                    }
                }
                adapter.addCartItems(finalData)
                hideProgressDialog()
            }
        })
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
