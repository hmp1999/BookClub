package com.iser.project313.ui.orders

import com.iser.project313.ui.CartDetail
import com.iser.project313.ui.cart.BillInfo
import java.io.Serializable

data class OrderDetails(var orderId: String) : Serializable {
    var products : ArrayList<CartDetail> = ArrayList()
    var billInfo: BillInfo? = null
    var deliveredTo: String? = null
    var deliveredAt: String? = null
    var contactInfoPhone: String? = null
    var listPrice: Double = 0.0
    var sellingPrice: Double = 0.0
    var extraDiscount: Double = 0.0
    var shippingCharge: Double = 0.0
    var totalAmount: Double = 0.0
    var quantity : Int = 0
    var orderTitle : String? =null
    var orderedOn: String? = null
    var createdBy : String? = null

    constructor(): this("")

}