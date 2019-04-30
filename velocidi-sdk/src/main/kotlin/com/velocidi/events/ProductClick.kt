package com.velocidi.events

import kotlinx.serialization.Serializable

@Serializable
data class ProductClick(
    override val siteId: String,
    override val clientId: String,
    val products: List<Product> = emptyList()
) : TrackingEvent("productClick") {
    override fun serialize(): String =
        jsonSerilizer().stringify(serializer(), this)
}
