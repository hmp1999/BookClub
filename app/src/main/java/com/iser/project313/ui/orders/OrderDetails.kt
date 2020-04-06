package com.iser.project313.ui.orders

import com.iser.project313.ui.home.BookInfo

data class OrderDetails(var orderId: String) {
    lateinit var productId: String
    lateinit var productInfo: BookInfo
    var deliveredTo: String? = null
    var deliveredAt: String? = null
    var contactInfoPhone: String? = null
    var listPrice: Int = 0
    var sellingPrice: Int = 0
    var extraDiscount: Int = 0
    var shippingCharge: Int = 0
    var totalAmount: Int = 0
    var quantity : Int = 0
}