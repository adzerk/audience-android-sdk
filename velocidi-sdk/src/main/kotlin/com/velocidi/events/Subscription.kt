package com.velocidi.events

import kotlinx.serialization.Serializable

@Serializable
data class Subscription(
    override val siteId: String,
    override val clientId: String? = null,
    val products: List<Product> = emptyList(),
    val transaction: Transaction? = null
) : TrackingEvent("subscription", siteId, clientId) {
    override fun serialize(): String =
        jsonSerilizer().stringify(serializer(), this)
}
