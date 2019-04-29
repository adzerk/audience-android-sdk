package com.velocidi.events

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
data class Purchase(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    @Optional val product: Product? = null,
    @Optional val transaction: Transaction? = null
) : TrackingEvent("purchase", siteId, clientId) {
    override fun serialize(): String =
        jsonSerilizer().stringify(serializer(), this)
}
