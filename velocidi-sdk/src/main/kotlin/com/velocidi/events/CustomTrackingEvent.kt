package com.velocidi.events

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import org.json.JSONObject

@Serializable
class CustomTrackingEvent(@Transient val json: JSONObject = JSONObject()) :
    TrackingEvent(json.getString("type"), extractAttribute(json, "siteId"), extractAttribute(json, "clientId")) {

    @Serializer(forClass = CustomTrackingEvent::class)
    companion object : SerializationStrategy<CustomTrackingEvent> {
        override val descriptor: SerialDescriptor = StringDescriptor.withName("CustomTrackingEvent")

        override fun serialize(encoder: Encoder, obj: CustomTrackingEvent) {
            encoder.encodeString(obj.json.toString())
        }

        fun extractAttribute(json: JSONObject, attr: String): String? {
            return try {
                json.getString(attr)
            } catch (e: org.json.JSONException) {
                null
            }
        }

    }
}