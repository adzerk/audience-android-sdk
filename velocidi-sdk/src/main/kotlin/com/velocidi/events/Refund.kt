package com.velocidi.events

data class Refund(
    override val siteId: String,
    override val clientId: String,
    val refundType: String,
    val products: List<Product>? = emptyList(),
    val transaction: Transaction? = null
) : TrackingEvent("refund") {
    override fun serialize(): String =
        defaultGson().toJson(this)
}
