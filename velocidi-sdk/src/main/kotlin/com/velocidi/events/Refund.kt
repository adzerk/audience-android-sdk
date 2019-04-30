package com.velocidi.events

import kotlinx.serialization.Serializable

@Serializable
data class Refund(
    override val siteId: String,
    override val clientId: String,
    val refundType: String,
    val products: List<Product>? = emptyList(),
    val transaction: Transaction? = null
) : TrackingEvent("refund") {
    override fun serialize(): String =
        jsonSerilizer().stringify(serializer(), this)
}
