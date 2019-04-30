package com.velocidi.events

import kotlinx.serialization.Serializable

@Serializable
data class ProductViewDetails(
    override val siteId: String,
    override val clientId: String,
    val products: List<Product> = emptyList()
) : TrackingEvent("productViewDetails") {
    override fun serialize(): String =
        jsonSerilizer().stringify(serializer(), this)
}
