package com.velocidi.events

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
class PageView(
    @Optional override val siteId: String? = null,
    @Optional override val clientId: String? = null,
    @Optional val location: String? = null,
    @Optional val title: String? = null,
    @Optional val pageType: String? = null,
    @Optional val category: String? = null
) : TrackingEvent("pageView", siteId, clientId)