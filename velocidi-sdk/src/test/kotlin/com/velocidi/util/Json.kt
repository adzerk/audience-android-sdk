package com.velocidi.util

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.parse
import kotlinx.serialization.stringify

@ImplicitReflectionSerializer
fun String.prettyPrintJson(): String {
    return Json.indented.stringify(Json.parse<JsonObject>(this))
}
