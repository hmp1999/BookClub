package com.iser.project313.ui

import android.os.Bundle
import android.text.TextUtils
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.iser.project313.R
import com.iser.project313.ui.cart.CartAdapter
import com.iser.project313.ui.orders.OrderDetails
import kotlinx.android.synthetic.main.activity_order_details.*
import kotlinx.android.synthetic.main.book_listing_main_content.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class OrderDetailsActivity : BaseActivity() {
    lateinit var orderDetails : OrderDetails
    private lateinit var adapter: CartAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initViews()
        initData()
    }

    private fun initData(){
        orderDetails = intent.getSerializableExtra("ORDER") as OrderDetails
        val dataSet : ArrayList<Any> = ArrayList()
        if (orderDetails!=null && orderDetails.products!=null && orderDetails.billInfo!=null){
            dataSet.add(orderDetails.billInfo!!)
        }
        dataSet.addAll(orderDetails.products)
        adapter.addCartItems(dataSet)
    }

    private fun initViews(){
        rv_book_list?.setPadding(0,0,0,200)
        rv_book_list?.clipToPadding = false
        adapter = CartAdapter(ArrayList(), AdapterView.OnItemClickListener { parent, clickedView, position, id ->

        },showRemoveIcon = false)
        rv_book_list?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        rv_book_list?.adapter = adapter
        swipeToRefresh?.setOnRefreshListener {
            swipeToRefresh?.isRefreshing= false
        }
        btn_checkout?.setOnClickListener {
            placeOrder()
        }
    }

    private fun placeOrder() {
        showProgressDialog()
        if (validData()){
            if (FirebaseAuth.getInstance().currentUser!=null){
                val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
                val rootRef = firebaseDatabase.reference
                val orders = rootRef.child("orders")
                val newNode = orders.push()
                val key = newNode.key
                val orderDetail = OrderDetails(key.toString())
                orderDetail.billInfo = this.orderDetails.billInfo
                orderDetail.products = this.orderDetails.products
                orderDetail.contactInfoPhone = edt_phone.text.toString()
                orderDetail.deliveredAt = edt_address.text.toString()
                orderDetail.deliveredTo = FirebaseAuth.getInstance().currentUser?.email
                orderDetail.quantity = this.orderDetails.products.size
                orderDetail.totalAmount = this.orderDetails.billInfo?.totalAmont ?: 0.0
                orderDetail.orderedOn = getCurrentDate()
                orderDetail.createdBy = FirebaseAuth.getInstance().currentUser?.email
                newNode.setValue(orderDetail){databaseError, databaseReference ->
                    if (databaseError == null){
                        Snackbar.make(edt_address, "Order Placed successfully", Snackbar.LENGTH_LONG)
                            .show()
                        deleteFromCart()
                        finish()
                    }else {
                        Snackbar.make(
                            edt_address,
                            "Something went wrong \n try again",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    hideProgressDialog()
                }
            }else {
                hideProgressDialog()
            }
        }else {
            hideProgressDialog()
        }
    }

    private fun deleteFromCart() {
        showProgressDialog()
        val dataSet = orderDetails.products
        var key : String? = null
        dataSet.forEach { item ->
            key = item.cartId
            FirebaseDatabase.getInstance().getReference("bag/$key").removeValue()
        }
        hideProgressDialog()
    }

    private fun validData(): Boolean {
        if (edt_address.text == null || TextUtils.isEmpty(edt_address.text.toString())){
            til_address.error = "Address is required"
            return false
        }
        if (edt_phone.text == null || TextUtils.isEmpty(edt_phone.text.toString())){
            til_phone.error = "Phone is required"
            return false
        }
        return true
    }

    private fun getCurrentDate() : String {
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val date = Date()
        return formatter.format(date)
    }
}
