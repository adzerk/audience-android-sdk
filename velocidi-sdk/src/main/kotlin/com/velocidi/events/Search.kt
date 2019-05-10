package com.velocidi.events

data class Search(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("search") {
    var query: String? = null
}
