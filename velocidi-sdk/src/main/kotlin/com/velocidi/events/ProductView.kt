package com.velocidi.events

data class ProductView(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("productView") {
    var products: List<Product> = emptyList()
}
