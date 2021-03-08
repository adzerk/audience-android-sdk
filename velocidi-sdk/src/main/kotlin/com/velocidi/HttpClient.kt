package com.velocidi

import android.net.Uri
import android.util.Log
import com.velocidi.Util.appendToUrl
import java.io.IOException
import java.lang.Exception
import okhttp3.*
import okhttp3.Request
import org.json.JSONObject

/**
 * Http Client based on Android Volley
 *
 */
internal class HttpClient {
    private val client =
        OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .build()

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
        url: Uri,
        payload: JSONObject? = null,
        parameters: Map<String, String> = emptyMap(),
        headers: Map<String, String> = emptyMap(),
        listener: ResponseListener = defaultListener
    ) {
        val urlWithParams = url.appendToUrl(parameters)
        val body = if (verb == Verb.POST) {
            RequestBody.create(JSON_MEDIA_TYPE, payload?.toString() ?: "")
        } else null

        val req =
            Request.Builder()
                .headers(Headers.of(headers.toMutableMap()))
                .url(urlWithParams.toString())
                .method(verb.name, body)
                .build()

        client.newCall(req).enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    listener.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        listener.onError(Exception("Unexpected code $response"))
                    } else {
                        listener.onResponse("Success: ${response.message()}")
                    }
                }
            }
        )
    }

    enum class Verb { GET, POST }

    companion object {

        private val JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8")

        val defaultListener = object : ResponseListener {
            override fun onResponse(response: String) {
                Log.v(Constants.LOG_TAG, response)
            }

            override fun onError(ex: Exception) {
                Log.v(Constants.LOG_TAG, ex.toString())
            }
        }
    }
}

internal interface ResponseListener {
    fun onError(ex: Exception)

    fun onResponse(response: String)
}
