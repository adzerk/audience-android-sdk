package com.velocidi.events

import kotlinx.serialization.Serializable

@Serializable
data class Search(
    override val siteId: String,
    override val clientId: String,
    val query: String? = null
) : TrackingEvent("search") {
    override fun serialize(): String =
        jsonSerilizer().stringify(serializer(), this)
}
