package com.velocidi.events

data class ProductClick(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("productClick") {
    var products: List<Product> = emptyList()
}
