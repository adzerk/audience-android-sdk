package com.velocidi

import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.Response.Listener
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import java.io.File
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.nio.charset.Charset
import com.velocidi.Util.appendToUrl

/**
 * Http Client based on Android Volley
 *
 */
internal class HttpClient {
    private val cache = DiskBasedCache(File(Constants.CACHE_DIR), 1024 * 1024) // 1MB cap

    private val network = BasicNetwork(HurlStack())

    val requestQueue = RequestQueue(cache, network).apply {
        start()
    }

    /**
     * Sends an http request
     *
     * @param verb Http method - GET / POST
     * @param url request URL
     * @param payload request payload
     * @param parameters additional parameters to append to url
     * @param headers request headers
     * @param listener listener to receive the request response
     */
    fun sendRequest(
        verb: Verb,
        url: URL,
        payload: JSONObject? = null,
        parameters: Map<String, String> = emptyMap(),
        headers: Map<String, String> = emptyMap(),
        listener: ResponseListener = defaultListener
    ) {
        val successListener = Listener<String> { response -> listener.onResponse(response) }
        val errorListener = Response.ErrorListener { error -> listener.onError(error) }

        val urlWithParams = url.appendToUrl(parameters)

        val stringRequest = object : StringRequest(
            verb.i, urlWithParams.toString(),
            successListener, errorListener
        ) {

            override fun getHeaders() =
                headers.toMutableMap()

            override fun getBodyContentType(): String =
                "application/json"

            override fun getBody(): ByteArray? =
                payload?.toString()?.toByteArray(Charset.defaultCharset())
        }

        requestQueue.add(stringRequest)
    }

    enum class Verb(val i: Int) {
        GET(0),
        POST(1)
    }

    companion object {
        val defaultListener = object : ResponseListener {
            override fun onResponse(response: String) {
                Log.i(Constants.LOG_TAG, response)
            }

            override fun onError(ex: Exception) {
                Log.i(Constants.LOG_TAG, ex.toString())
            }
        }
    }
}

interface ResponseListener {
    fun onError(ex: Exception)

    fun onResponse(response: String)
}
