package com.iser.project313.ui.home

import java.io.Serializable

class BookInfo(
    var title: String,
    var price: String,
    resourceId: Int,
    var author: String,
    var shortDesc: String?,
    var longDesc: String?,
    var createdBy: String? = ""
) : Serializable {
    var rating: Float = 0.0F
    var resourceId: Int = 0

    init {
        this.resourceId = resourceId
    }
}