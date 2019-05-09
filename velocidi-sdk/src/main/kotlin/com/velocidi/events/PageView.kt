package com.velocidi.events

class PageView(
    override val siteId: String,
    override val clientId: String
) : TrackingEvent("pageView") {
    var location: String? = null
    var title: String? = null
    var pageType: String? = null
    var category: String? = null
}
