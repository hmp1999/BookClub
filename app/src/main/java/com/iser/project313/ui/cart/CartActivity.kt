package com.iser.project313.ui.cart

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
import kotlinx.android.synthetic.main.activity_book_detail.*
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_cart.toolbar
import kotlinx.android.synthetic.main.book_listing_main_content.*
import java.math.RoundingMode
import kotlin.random.Random

class CartActivity : BaseActivity() {
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        title = ""
        initViews()
        getCartDetails()
    }

    private fun getCartDetails() {
        showProgressDialog()
        val data = ArrayList<Any>()
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        FirebaseDatabase.getInstance().getReference("/bag").addListenerForSingleValueEvent(object :
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
                /*data.forEach {
                    if (it.createdBy!=null && it.createdBy.equals(userEmail)){
                        true
                    }
                }*/
                data.filter {
                    it as CartDetail
                    it.createdBy.equals(userEmail)
                }
                val billInfo : BillInfo = BillInfo(Random.nextInt().toString())
                var quantity = 0
                var totalAmount : Double = 0.0
                data.forEach {
                    it as CartDetail
                    quantity++
                    totalAmount += it.productInfo.price.toDouble()
                }
                billInfo.quantity = quantity
                billInfo.totalAmont = totalAmount.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble()
                data.add(billInfo)
                setPriceLabel(billInfo)

                if (data.size > 1){
                    adapter.addCartItems(data)
                    img_no_data?.visibility = View.GONE
                    layout_price_details?.visibility =  View.VISIBLE
                }else{
                    img_no_data?.visibility = View.VISIBLE
                    layout_price_details?.visibility = View.GONE
                }
                hideProgressDialog()
            }
        })
    }

    private fun setPriceLabel(billInfo: BillInfo) {
        tv_total_price?.text = this.getString(R.string.currency_price).replaceFirst("#currency", "$")
            .replaceFirst("#price",billInfo.totalAmont.toString(), false )
    }

    private fun initViews() {
        rv_book_list?.setPadding(0,0,0,200)
        rv_book_list?.clipToPadding = false
        adapter = CartAdapter(ArrayList(), AdapterView.OnItemClickListener { parent, clickedView, position, id ->
            if (clickedView.id == R.id.btn_delete_from_cart)
                deleteCartItem(position, adapter.getItem(position) as CartDetail)
        },showRemoveIcon = true)
        rv_book_list?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        rv_book_list?.adapter = adapter
        swipeToRefresh?.setOnRefreshListener {
            getCartDetails()
            swipeToRefresh?.isRefreshing= false
        }
    }

    private fun deleteCartItem(position : Int ,item: CartDetail) {
        showProgressDialog()
        val key = item.cartId
        FirebaseDatabase.getInstance().getReference("bag/$key").removeValue()
            .addOnCompleteListener {
                Snackbar.make(tv_total_price, "Item has been removed from cart", Snackbar.LENGTH_LONG)
                    .show()
                adapter.removeItem(position)
                hideProgressDialog()
                getCartDetails()
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
