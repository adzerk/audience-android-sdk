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
     * Serializes the current data model to JSON
     */
    open fun serialize(): String {
        return defaultGson.toJson(this)
    }

    companion object {
        val defaultGson =
            GsonBuilder()
                .registerTypeHierarchyAdapter(Collection::class.java, CollectionAdapter())
                .create()
    }

    /**
     * Gson doesn't have the concept of default values.
     * This Adapter excluded our most common default values from serialization - empty collections and null
     */
    internal class CollectionAdapter : JsonSerializer<List<*>> {
        override fun serialize(
            src: List<*>?,
            typeOfSrc: Type,
            context: JsonSerializationContext
        ): JsonElement? {
            if (src == null || src.isEmpty())
                return null

            val array = JsonArray()

            for (child in src) {
                val element = context.serialize(child)
                array.add(element)
            }

            return array
        }
    }
}
