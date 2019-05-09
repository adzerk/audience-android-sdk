package com.velocidi.events

data class Refund(
    override val siteId: String,
    override val clientId: String,
    val refundType: String
) : TrackingEvent("refund") {
    var products: List<Product>? = emptyList()
    var transaction: Transaction? = null
}
