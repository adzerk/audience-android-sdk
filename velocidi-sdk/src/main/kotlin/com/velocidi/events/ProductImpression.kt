package com.velocidi.events

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ProductImpression(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    @Optional val products: List<Product>? = emptyList()
) : TrackingEvent("productImpression", siteId, clientId) {
    override fun serialize(): String =
        Json.plain.stringify(serializer(), this)
}
