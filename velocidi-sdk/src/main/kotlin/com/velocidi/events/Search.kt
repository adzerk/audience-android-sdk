package com.velocidi.events

import kotlinx.serialization.Serializable

@Serializable
data class Search(
    override val siteId: String,
    override val clientId: String? = null,
    val query: String? = null
) : TrackingEvent("search", siteId, clientId) {
    override fun serialize(): String =
        jsonSerilizer().stringify(serializer(), this)
}
