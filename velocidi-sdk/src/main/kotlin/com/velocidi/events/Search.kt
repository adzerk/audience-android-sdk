package com.velocidi.events

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
data class Search(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    @Optional val query: String? = null
) : TrackingEvent("search", siteId, clientId) {
    override fun serialize(): String =
        jsonSerilizer().stringify(serializer(), this)
}
