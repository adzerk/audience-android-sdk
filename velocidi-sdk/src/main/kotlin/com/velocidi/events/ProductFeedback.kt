package com.velocidi.events

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ProductFeedback(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    @Optional val product: Product? = null,
    @Optional val rating: Double? = null,
    @Optional val feedback: String? = null
) : TrackingEvent("productFeedback", siteId, clientId) {
    override fun serialize(): String =
        Json.plain.stringify(serializer(), this)
}
