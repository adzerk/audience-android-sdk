package com.velocidi.events

import com.google.gson.*
import org.json.JSONObject
import java.lang.reflect.Type

class CustomTrackingEvent(
    val eventType: String, // Must be a declared as a propriety so kotlinx can properly serialize this class
    override val siteId: String,
    override val clientId: String,
    @Transient val extraAttributes: JSONObject = JSONObject()
) : TrackingEvent(eventType) {

    override fun serialize(): String {
        val gson =
            GsonBuilder()
                .registerTypeAdapter(CustomTrackingEvent::class.java, CustomTrackingEventSerializer)
                .create()
        return gson.toJson(this)
    }

    internal companion object {
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

object CustomTrackingEventSerializer : JsonSerializer<CustomTrackingEvent> {
    override fun serialize(
        src: CustomTrackingEvent?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val mergedJson = JSONObject(src?.extraAttributes.toString())

        mergedJson
            .put(CustomTrackingEvent.typeField, src?.type)
            .put(CustomTrackingEvent.siteIdField, src?.siteId)
            .put(CustomTrackingEvent.clientIdField, src?.clientId)

        return JsonParser().parse(mergedJson.toString())
    }
}
