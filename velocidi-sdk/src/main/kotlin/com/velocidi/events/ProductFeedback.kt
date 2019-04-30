package com.velocidi.events

import kotlinx.serialization.Serializable

@Serializable
data class ProductFeedback(
    override val siteId: String,
    override val clientId: String,
    val products: List<Product> = emptyList(),
    val rating: Double? = null,
    val feedback: String? = null
) : TrackingEvent("productFeedback") {
    override fun serialize(): String =
        jsonSerilizer().stringify(serializer(), this)
}
