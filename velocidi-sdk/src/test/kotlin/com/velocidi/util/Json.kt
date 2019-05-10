package com.velocidi.util

import com.google.gson.JsonParser
import com.google.gson.GsonBuilder

fun String.prettyPrintJson(): String {
    val gson = GsonBuilder().setPrettyPrinting().create()
    val jsonElement = JsonParser().parse(this)
    return gson.toJson(jsonElement)
}
