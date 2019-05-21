package com.velocidi.events

import com.google.gson.*
import org.json.JSONObject
import java.lang.reflect.Type

class CustomTrackingEvent(
    eventType: String,
    override val siteId: String,
    override val clientId: String,
    val extraAttributes: JSONObject = JSONObject()
) : TrackingEvent(eventType) {

    @Transient
    override val gson = defaultGson

    fun appendProperty(key: String, value: String) =
        extraAttributes.accumulate(key, value)

    internal companion object {
        val defaultGson =
            TrackingEvent.defaultGson.newBuilder()
                .registerTypeAdapter(CustomTrackingEvent::class.java, CustomTrackingEventSerializer)
                .create()

        const val typeField = "type"
        const val siteIdField = "siteId"
        const val clientIdField = "clientId"

        object CustomTrackingEventSerializer : JsonSerializer<CustomTrackingEvent> {
            override fun serialize(
                src: CustomTrackingEvent?,
                typeOfSrc: Type?,
                context: JsonSerializationContext?
            ): JsonElement {
                val json = JsonParser().parse(src?.extraAttributes.toString()).asJsonObject

                json.addProperty(typeField, src?.type)
                json.addProperty(siteIdField, src?.siteId)
                json.addProperty(clientIdField, src?.clientId)

                return json
            }
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

        val type = jsonObj.getString(CustomTrackingEvent.typeField)
        val siteId = jsonObj.getString(CustomTrackingEvent.siteIdField)
        val clientId = jsonObj.getString(CustomTrackingEvent.clientIdField)

        jsonObj.remove(CustomTrackingEvent.typeField)
        jsonObj.remove(CustomTrackingEvent.siteIdField)
        jsonObj.remove(CustomTrackingEvent.clientIdField)

        return CustomTrackingEvent(type, siteId, clientId, jsonObj)
    }
}
