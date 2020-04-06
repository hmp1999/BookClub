package com.iser.project313.ui

import com.iser.project313.ui.home.BookInfo

data class CartDetail(var cartId : String){
    lateinit var productId : String
    lateinit var productInfo : BookInfo
    var createdBy : String? = null
    constructor(): this("")
}