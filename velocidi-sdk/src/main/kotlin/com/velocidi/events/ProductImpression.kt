package com.velocidi.events

import kotlinx.serialization.Serializable

@Serializable
data class ProductImpression(
    override val siteId: String,
    override val clientId: String,
    val products: List<Product>? = emptyList()
) : TrackingEvent("productImpression") {
    override fun serialize(): String =
        jsonSerilizer().stringify(serializer(), this)
}
