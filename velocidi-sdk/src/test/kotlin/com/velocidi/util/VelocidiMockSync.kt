package com.velocidi.util

import android.content.Context
import com.velocidi.AdvertisingInfo
import com.velocidi.Config
import com.velocidi.Velocidi

internal class VelocidiMockSync(
    config: Config,
    context: Context,
    advertisingInfo: AdvertisingInfo = AdvertisingInfo("123", true)
) : Velocidi(config, context) {

    init {
        this.adInfo = advertisingInfo
    }

    override fun fetchAndSetAdvertisingId(context: Context) {
        return
    }
}
