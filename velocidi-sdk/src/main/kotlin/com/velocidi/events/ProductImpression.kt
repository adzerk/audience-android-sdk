package com.velocidi.events

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
data class ProductImpression(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    @Optional val products: List<Product>? = emptyList()
) : TrackingEvent("productImpression", siteId, clientId) {
    override fun serialize(): String =
        jsonSerilizer().stringify(serializer(), this)
}
