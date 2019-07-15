package com.velocidi.events

import com.google.gson.*
import java.lang.reflect.Type

/**
 * Base event
 *
 * @property type Event type
 */
abstract class TrackingEvent(
    internal val type: String
) {
    /**
     * Client's site identifier
     */
    abstract val siteId: String

    /**
     * Client identifier
     */
    abstract val clientId: String

    @Transient
    internal open val gson = defaultGson

    /**
     * Serializes the current data model to query parameters
     */
    internal open fun toQueryParams(): Map<String, String> {
        val evt = gson.toJsonTree(this)

        fun toQueryParamsAux(elem: JsonElement, qs: MutableMap<String, String>, path: String) {
            when (elem) {
                is JsonObject ->
                    for (key in elem.keySet()) {
                        val k = if (path.isEmpty()) key else "[$key]"
                        toQueryParamsAux(elem[key], qs, path + k)
                    }
                is JsonArray ->
                    for ((index, value) in elem.asIterable().withIndex()) {
                        val k = if (path.isEmpty()) index.toString() else "[$index]"
                        toQueryParamsAux(value, qs, path + k)
                    }
                is JsonPrimitive ->
                    qs[path] = elem.asString
            }
        }

        val qs = mutableMapOf<String, String>()
        toQueryParamsAux(evt, qs, "")

        return qs
    }

    internal companion object {
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
