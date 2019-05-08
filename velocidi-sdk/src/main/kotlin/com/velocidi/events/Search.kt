package com.velocidi.events

data class Search(
    override val siteId: String,
    override val clientId: String,
    val query: String? = null
) : TrackingEvent("search") {
    override fun serialize(): String =
        defaultGson().toJson(this)
}
