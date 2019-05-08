package com.velocidi.events

import com.google.gson.JsonElement
import com.google.gson.JsonArray
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import com.google.gson.GsonBuilder

abstract class TrackingEvent(
    val type: String
) {
    abstract val siteId: String
    abstract val clientId: String

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
    protected fun defaultGson() = GsonBuilder().registerTypeHierarchyAdapter(
        Collection::class.java,
        CollectionAdapter()
    ).create()
}

internal class CollectionAdapter : JsonSerializer<List<*>> {
    override fun serialize(src: List<*>?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement? {
        if (src == null || src.isEmpty())
        // exclusion is made here
            return null

        val array = JsonArray()

        for (child in src) {
            val element = context.serialize(child)
            array.add(element)
        }

        return array
    }
}
