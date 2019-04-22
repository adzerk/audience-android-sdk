package com.velocidi

import android.content.Context
import android.os.AsyncTask
import android.util.Log

data class AdvertisingInfo(val id: String, val shouldTrack: Boolean)

internal class GetAdvertisingIdTask(val listener: (AdvertisingInfo) -> Unit) : AsyncTask<Context, Void, AdvertisingInfo>() {

    @Throws(Exception::class)
    fun getAdvertisingId(context: Context): AdvertisingInfo {
        val advertisingInfo =
            Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient")
                .getMethod("getAdvertisingIdInfo", Context::class.java)
                .invoke(null, context)
        val isLimitAdTrackingEnabled =
            advertisingInfo.javaClass
                .getMethod("isLimitAdTrackingEnabled")
                .invoke(advertisingInfo) as Boolean

        val advertisingId = advertisingInfo.javaClass.getMethod("getId").invoke(advertisingInfo) as String

        if (isLimitAdTrackingEnabled) {
            Log.w(
                Constants.LOG_TAG,
                "Not collecting advertising ID because isLimitAdTrackingEnabled (Google Play Services) is true."
            )
            return AdvertisingInfo(advertisingId, false)
        }

        return AdvertisingInfo(advertisingId, true)
    }

    override fun doInBackground(vararg contexts: Context): AdvertisingInfo? {
        val context = contexts[0]

        return try {
            getAdvertisingId(context)
        } catch (e: Exception) {
            Log.e(Constants.LOG_TAG, "Unable to collect advertising ID from Google Play Services.")
            null
        }
    }

    override fun onPostExecute(info: AdvertisingInfo) {
        super.onPostExecute(info)

        listener(info)
    }
}
