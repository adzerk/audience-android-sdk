package com.velocidi.events

data class Purchase(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("purchase") {
    var products: List<Product> = emptyList()
    var transaction: Transaction? = null
}
