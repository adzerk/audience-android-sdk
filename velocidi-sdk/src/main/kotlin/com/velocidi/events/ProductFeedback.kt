package com.velocidi.events

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
data class ProductFeedback(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    @Optional val products: List<Product> = emptyList(),
    @Optional val rating: Double? = null,
    @Optional val feedback: String? = null
) : TrackingEvent("productFeedback", siteId, clientId) {
    override fun serialize(): String =
        jsonSerilizer().stringify(serializer(), this)
}
