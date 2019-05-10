package com.velocidi.events

data class Subscription(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("subscription") {
    var products: List<Product> = emptyList()
    var transaction: Transaction? = null
}
