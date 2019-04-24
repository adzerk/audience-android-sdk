package com.velocidi.util

import android.content.Context
import com.velocidi.AdvertisingInfo
import com.velocidi.Config
import com.velocidi.Velocidi
import org.robolectric.Robolectric

internal class VelocidiMockAsync(
    config: Config,
    context: Context,
    val advertisingInfo: AdvertisingInfo = AdvertisingInfo("123", true)
) : Velocidi(config, context) {

    override fun fetchAndSetAdvertisingId(context: Context) {
        DelayedAsyncTask {
            this.adInfo = advertisingInfo
            emptyTaskQueue()
        }.execute()

        // We need to explicit force start the background thread
        Robolectric.flushBackgroundThreadScheduler()
    }
}
