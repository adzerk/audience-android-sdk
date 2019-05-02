package com.velocidi.events

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import org.json.JSONObject

@Serializable
class CustomTrackingEvent(
    val eventType: String, // Must be a declared as a propriety so kotlinx can properly serialize this class
    override val siteId: String,
    override val clientId: String,
    @Transient val extraAttributes: JSONObject = JSONObject()
) : TrackingEvent(eventType) {

    // In this specific case we need to unescape the json string
    override fun serialize(): String =
        jsonSerilizer().stringify(serializer(), this).trim('"').replace("\\", "")

    @Serializer(forClass = CustomTrackingEvent::class)
    internal companion object : SerializationStrategy<CustomTrackingEvent> {
        override val descriptor: SerialDescriptor = StringDescriptor.withName("CustomTrackingEvent")

        override fun serialize(encoder: Encoder, obj: CustomTrackingEvent) {

            val mergedJson = JSONObject(obj.extraAttributes.toString())

            mergedJson
                .put(typeField, obj.type)
                .put(siteIdField, obj.siteId)
                .put(clientIdField, obj.clientId)

            encoder.encodeString(mergedJson.toString())
        }

        const val typeField = "type"
        const val siteIdField = "siteId"
        const val clientIdField = "clientId"
    }
}

object CustomTrackingEventFactory {
    /**
     * Factory method to create a CustomTrackingEvent from a JSON
     *
     * @param json Json to be parsed
     * @return Properly populated CustomTrackingEvent
     */
    fun buildFromJSON(json: String): CustomTrackingEvent {
        val jsonObj = JSONObject(json)

        val type = jsonObj.getString(CustomTrackingEvent.typeField)
        val siteId = jsonObj.getString(CustomTrackingEvent.siteIdField)
        val clientId = jsonObj.getString(CustomTrackingEvent.clientIdField)

        jsonObj.remove(CustomTrackingEvent.typeField)
        jsonObj.remove(CustomTrackingEvent.siteIdField)
        jsonObj.remove(CustomTrackingEvent.clientIdField)

        return CustomTrackingEvent(type, siteId, clientId, jsonObj)
    }
}
