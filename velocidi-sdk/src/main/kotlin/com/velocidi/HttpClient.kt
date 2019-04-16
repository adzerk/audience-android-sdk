package com.velocidi

import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import java.io.File
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.nio.charset.Charset

class HttpClient {
    private val cache = DiskBasedCache(File(Constants.CACHE_DIR), 1024 * 1024) // 1MB cap

    private val network = BasicNetwork(HurlStack())

    val requestQueue = RequestQueue(cache, network).apply {
        start()
    }

    fun sendRequest(
        verb: Verb,
        url: URL,
        payload: JSONObject? = null,
        parameters: Map<String, String> = emptyMap(),
        headers: Map<String, String> = emptyMap(),
        listener: ResponseListener? = null
    ) {
        val successListener = Response.Listener<String> {
            response ->
                listener?.onResponse(response) ?: Log.i(Constants.LOG_TAG, response)
            }

        val errorListener = Response.ErrorListener {
            error ->
                listener?.onError(error) ?: Log.i(Constants.LOG_TAG, error.toString())
            }

        val urlWithParams = Util.appendToUrl(url, parameters)

        val stringRequest = object : StringRequest(
            verb.i, urlWithParams.toString(),
            successListener, errorListener
        ) {

            override fun getHeaders() =
                headers.toMutableMap()

            override fun getBodyContentType(): String =
                "application/json; charset=utf-8"

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
    fun onError(message: Exception)

    fun onResponse(response: String)
}
