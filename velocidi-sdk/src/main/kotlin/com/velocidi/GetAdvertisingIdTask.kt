package com.velocidi

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.ads.identifier.AdvertisingIdClient

data class AdvertisingInfo(val id: String, val shouldTrack: Boolean)

/**
 * Class responsible for obtaining the Google Advertising Id
 *
 * @property listener callback executed once the task concludes
 */
internal open class GetAdvertisingIdTask(val listener: (AdvertisingInfo) -> Unit) : AsyncTask<Context, Void, AdvertisingInfo>() {

    @Throws(Exception::class)
    open fun getAdvertisingId(context: Context): AdvertisingInfo {

        val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context.applicationContext)

        if (adInfo.isLimitAdTrackingEnabled) {
            Log.w(
                Constants.LOG_TAG,
                "Not collecting advertising ID because isLimitAdTrackingEnabled (Google Play Services) is true."
            )
            return AdvertisingInfo(adInfo.id, false)
        }
        return AdvertisingInfo(adInfo.id, true)
    }

    override fun doInBackground(vararg contexts: Context): AdvertisingInfo {
        val context = contexts[0]

        return try {
            getAdvertisingId(context)
        } catch (e: Exception) {
            throw Exception("Unable to collect advertising ID from Google Play Services.")
        }
    }

    override fun onPostExecute(info: AdvertisingInfo) {
        super.onPostExecute(info)

        listener(info)
    }
}
