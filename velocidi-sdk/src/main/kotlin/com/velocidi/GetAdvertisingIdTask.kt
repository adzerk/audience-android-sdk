package com.velocidi

import android.content.Context
import android.os.AsyncTask
import android.util.Log


interface AdvertisingIdListener {
    fun fetchAdvertisingIdCompleted(advertisingInfo: Pair<String, Boolean>? )
}

internal class GetAdvertisingIdTask(val listener: AdvertisingIdListener) : AsyncTask<Context, Void, Pair<String, Boolean>>() {

    @Throws(Exception::class)
    fun getGooglePlayServicesAdvertisingID(context: Context): Pair<String, Boolean> {
        val advertisingInfo = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient")
            .getMethod("getAdvertisingIdInfo", Context::class.java!!)
            .invoke(null, context)
        val isLimitAdTrackingEnabled = advertisingInfo.javaClass
            .getMethod("isLimitAdTrackingEnabled")
            .invoke(advertisingInfo) as Boolean

        val advertisingId = advertisingInfo.javaClass.getMethod("getId").invoke(advertisingInfo) as String

        if (isLimitAdTrackingEnabled) {
            Log.d("SDK",
                "Not collecting advertising ID because isLimitAdTrackingEnabled (Google Play Services) is true."
            )
            return Pair(advertisingId, false)
        }

        return Pair(advertisingId, true)
    }

    override fun doInBackground(vararg contexts: Context): Pair<String, Boolean>? {
        val context = contexts[0]
        try {
            return getGooglePlayServicesAdvertisingID(context)
        } catch (e: Exception) {
            Log.e("SDK", "Unable to collect advertising ID from Google Play Services.")
        }

        return null
    }

    override fun onPostExecute(info: Pair<String, Boolean>?) {
        super.onPostExecute(info)

        listener.fetchAdvertisingIdCompleted(info)
    }
}