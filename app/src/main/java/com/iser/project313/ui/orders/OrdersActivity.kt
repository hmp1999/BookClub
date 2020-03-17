package com.iser.project313.ui.orders

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.iser.project313.R
import kotlinx.android.synthetic.main.activity_personal_listing.*

class OrdersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "My Orders"
    }
}
