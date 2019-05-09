package com.velocidi.events

data class RemoveFromCart(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("removeFromCart") {
    var products: List<Product> = emptyList()
}
