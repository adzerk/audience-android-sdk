package com.velocidi.events

import kotlinx.serialization.Serializable

@Serializable
data class AddToCart(
    override val siteId: String,
    override val clientId: String? = null,
    val products: List<Product> = emptyList()
) : TrackingEvent("addToCart", siteId, clientId) {
    override fun serialize(): String =
        jsonSerilizer().stringify(serializer(), this)
}
