package com.velocidi.events

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
abstract class TrackingEvent(
    val type: String,
    @Transient open val siteId: String? = null,
    @Transient open val clientId: String? = null
)