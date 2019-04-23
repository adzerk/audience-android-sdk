package com.velocidi

import android.content.Context
import android.content.pm.PackageManager
import java.net.URI
import java.net.URISyntaxException
import java.net.URL

data class ApplicationInfo(
    val appName: String,
    val appVersion: String,
    val androidSDK: String,
    val device: String
)

object Util {
    /**
     * Obtains the necessary information to build the User-Agent(UA)
     *
     * @param context Android application context
     * @return ApplicationInfo with all the relevant information to build the UA
     */
    fun getApplicationInfo(context: Context): ApplicationInfo {
        val sdkVersion = android.os.Build.VERSION.SDK_INT.toString()
        val device = if (android.os.Build.MANUFACTURER != null && android.os.Build.MODEL != null) {
            android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL
        } else {
            android.os.Build.DEVICE ?: ""
        }

        return try {
            val packageManager = context.packageManager
            val packageName = context.packageName

            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            val packageInfo = packageManager.getPackageInfo(packageName, 0)

            val appName = packageManager.getApplicationLabel(applicationInfo) ?: packageName
            val appVersion = packageInfo.versionName ?: packageInfo.longVersionCode

            ApplicationInfo(appName.toString(), appVersion.toString(), sdkVersion, device)
        } catch (e: PackageManager.NameNotFoundException) {
            ApplicationInfo("Unknown app", "Unknown version", sdkVersion, device)
        }
    }

    /**
     * Verifies if the Android application has a specific permission
     *
     * @param context Android application context
     * @param permission Permission string
     * @return true - has permission; otherwise - false
     */
    fun checkPermission(context: Context, permission: String): Boolean {
        val res = context.checkCallingOrSelfPermission(permission)
        return res == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Adds parameters to an existing URL
     *
     * @param parameters Map (Key/Value) of parameters to be added
     * @return URL with the new parameters
     */
    @Throws(URISyntaxException::class)
    fun URL.appendToUrl(parameters: Map<String, String>): URL {
        if (parameters.isEmpty())
            return this

        val uri = this.toURI()
        val query = uri.query

        val params: List<String> = parameters.map { (k, v) -> "$k=$v" }

        val queryParams = (listOfNotNull(query) + params).joinToString("&")

        val newUrl = URI(uri.scheme, uri.authority, uri.path, queryParams, uri.fragment).toURL()
        return newUrl
    }
}
