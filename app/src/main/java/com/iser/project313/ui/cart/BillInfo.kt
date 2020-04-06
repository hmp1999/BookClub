package com.iser.project313.ui.cart

data class BillInfo(val billId : String){
    var totalAmont : Double = 0.0
    var discount : Double = 0.0
    var shippingCharge:Double = 0.0
    var quantity : Int = 0
}