package com.velocidi.events

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

@Serializable
abstract class TrackingEvent(
    val type: String,
    @Transient open val siteId: String? = null,
    @Transient open val clientId: String? = null
) {
    /**
     * Implicit serialization through reflection is still experimental
     * Until this feature is stable, its better to explicit pass the serializer
     *
     * To do that, each domain object that extends TrackingEvent must pass
     * the serializer in an explicit way when serializing to json.
     */
    abstract fun serialize(): String

    /**
     * This must be a function to avoid serializing the field
     */
    protected fun jsonSerilizer() = Json(JsonConfiguration.Stable.copy(encodeDefaults = false))
}
