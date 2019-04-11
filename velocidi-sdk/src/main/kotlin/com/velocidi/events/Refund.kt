package com.velocidi.events

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
data class Refund(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    val refundType: String,
    @Optional val products: List<Product>? = emptyList(),
    @Optional val transaction: Transaction? = null
) : TrackingEvent("refund", siteId, clientId)