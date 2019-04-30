package com.velocidi.events

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
data class ProductClick(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    @Optional val products: List<Product> = emptyList()
) : TrackingEvent("productClick", siteId, clientId) {
    override fun serialize(): String =
        jsonSerilizer().stringify(serializer(), this)
}
