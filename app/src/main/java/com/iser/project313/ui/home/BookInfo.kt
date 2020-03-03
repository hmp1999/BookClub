package com.iser.project313.ui.home

import java.io.Serializable

class BookInfo(
    title: String,
    price: String,
    resourceId: Int,
    author: String,
    shortDesc: String?,
    longDesc: String?
) : Serializable {
    var title: String = title
    var price: String = price
    var author: String = author
    var shortDesc: String? = shortDesc
    var longDesc: String? = longDesc
    var rating: Float = 0.0F
    var resourceId: Int = 0

    init {
        this.resourceId = resourceId
    }
}