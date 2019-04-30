package com.velocidi.events

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import org.json.JSONObject

@Serializable
class CustomTrackingEvent(
    private val eventType: String, // Must be a declared as a propriety so kotlinx can properly serialize this class
    override val siteId: String,
    override val clientId: String,
    @Transient val extraAttributes: JSONObject = JSONObject()
) : TrackingEvent(eventType) {

    override fun serialize(): String =
        jsonSerilizer().stringify(serializer(), this).trim('"').replace("\\", "")

    @Serializer(forClass = CustomTrackingEvent::class)
    companion object : SerializationStrategy<CustomTrackingEvent> {
        override val descriptor: SerialDescriptor = StringDescriptor.withName("CustomTrackingEvent")

        override fun serialize(encoder: Encoder, obj: CustomTrackingEvent) {

            val mergedJson = JSONObject(obj.extraAttributes.toString())

            mergedJson
                .put("type", obj.type)
                .put("siteId", obj.siteId)
                .put("clientId", obj.clientId)

            encoder.encodeString(mergedJson.toString())
        }
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
        val type = jsonObj.getString("type")
        val siteId = jsonObj.getString("siteId")
        val clientId = jsonObj.getString("clientId")

        jsonObj.remove("type")
        jsonObj.remove("siteId")
        jsonObj.remove("clientId")

        return CustomTrackingEvent(type, siteId, clientId, jsonObj)
    }
}
