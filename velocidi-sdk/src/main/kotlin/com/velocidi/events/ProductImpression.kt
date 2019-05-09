package com.velocidi.events

data class ProductImpression(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("productImpression") {
    var products: List<Product>? = emptyList()
}
