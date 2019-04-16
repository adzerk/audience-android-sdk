package com.velocidi

import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import java.io.File
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import java.nio.charset.Charset

class HttpClient {
    private val cache = DiskBasedCache(File(Constants.CACHE_DIR), 1024 * 1024) // 1MB cap

    private val network = BasicNetwork(HurlStack())

    val requestQueue = RequestQueue(cache, network).apply {
        start()
    }

    val headers = mutableMapOf(Pair("Content-Type", "application/json"))
    val defaultParams = mutableMapOf<String, String>()

    fun sendRequest(
        verb: Verb,
        url: String,
        payload: JSONObject? = null,
        listener: ResponseListener? = null
    ) {
        val successListener = Response.Listener<String> {
            response ->
                listener?.onResponse(response) ?: Log.i(Constants.LOG_TAG, response)
            }

        val errorListener = Response.ErrorListener {
            error ->
                listener?.onError(error.toString()) ?: Log.i(Constants.LOG_TAG, error.toString())
            }

        val urlWithParams = Util.appendToUrl(url, defaultParams)

        val stringRequest = object : StringRequest(
            verb.i, urlWithParams,
            successListener, errorListener
        ) {

            override fun getHeaders(): MutableMap<String, String> {
                return this@HttpClient.headers
            }

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray? =
                payload?.toString()?.toByteArray(Charset.defaultCharset())
        }

        requestQueue.add(stringRequest)
    }

    enum class Verb(val i: Int) {
        GET(0),
        POST(1)
    }
}

interface ResponseListener {
    fun onError(message: String)

    fun onResponse(response: String)
}
