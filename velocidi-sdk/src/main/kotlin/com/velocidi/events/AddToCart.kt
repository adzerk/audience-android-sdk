package com.velocidi.events

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class AddToCart(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    @Optional val product: Product? = null
) : TrackingEvent("addToCart", siteId, clientId) {
    override fun serialize(): String =
        Json.plain.stringify(serializer(), this)
}
