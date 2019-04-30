package com.velocidi.events

import kotlinx.serialization.Serializable

@Serializable
class PageView(
    override val siteId: String,
    override val clientId: String,
    val location: String? = null,
    val title: String? = null,
    val pageType: String? = null,
    val category: String? = null
) : TrackingEvent("pageView") {
    override fun serialize(): String =
        jsonSerilizer().stringify(serializer(), this)
}
