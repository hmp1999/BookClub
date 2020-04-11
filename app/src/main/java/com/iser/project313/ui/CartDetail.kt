package com.iser.project313.ui

import com.iser.project313.ui.home.BookInfo
import java.io.Serializable

data class CartDetail(var cartId : String) : Serializable{
    lateinit var productId : String
    lateinit var productInfo : BookInfo
    var createdBy : String? = null
    constructor(): this("")
}