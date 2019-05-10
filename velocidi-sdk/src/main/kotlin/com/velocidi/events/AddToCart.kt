package com.velocidi.events

class AddToCart(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("addToCart") {
    var products: List<Product> = emptyList()
}
