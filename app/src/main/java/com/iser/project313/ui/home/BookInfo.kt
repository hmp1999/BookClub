package com.iser.project313.ui.home

import java.io.Serializable
import kotlin.random.Random

class BookInfo(
    var title: String,
    var price: String,
    resourceId: Int,
    var author: String,
    var shortDesc: String?,
    var longDesc: String?,
    var createdBy: String? = "",
    var bookId: String?
) : Serializable {
    var rating: Float = 0.0F
    var resourceId: Int = 0
    var albumCover: String = ""

    init {
        this.resourceId = resourceId
    }

    constructor() : this(
        "",
        "0", 0, "", "", "", "", Random.nextInt().toString()
    )
}