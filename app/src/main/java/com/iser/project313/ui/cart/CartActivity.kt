package com.iser.project313.ui.cart

import android.os.Bundle
import com.iser.project313.R
import com.iser.project313.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_cart.*

class CartActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        setSupportActionBar(toolbar)

    }
}
