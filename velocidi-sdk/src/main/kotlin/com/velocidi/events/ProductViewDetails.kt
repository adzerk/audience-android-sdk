package com.velocidi.events

data class ProductViewDetails(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("productViewDetails") {
    var products: List<Product> = emptyList()
}
