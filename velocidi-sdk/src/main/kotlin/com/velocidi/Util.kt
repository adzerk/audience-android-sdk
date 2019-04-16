package com.velocidi

import android.content.Context
import android.content.pm.PackageManager
import java.net.URI
import java.net.URISyntaxException
import java.net.URL

data class ApplicationInfo(
    val appName: String = "",
    val appVersion: String = "",
    val androidSDK: String,
    val device: String
)

object Util {
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

            val appName = packageManager.getApplicationLabel(applicationInfo) ?: packageName ?: ""
            val appVersion = packageInfo.versionName ?: packageInfo.longVersionCode

            ApplicationInfo(appName.toString(), appVersion.toString(), sdkVersion, device)
        } catch (e: PackageManager.NameNotFoundException) {
            ApplicationInfo(androidSDK = sdkVersion, device = device)
        }
    }

    fun checkPermission(context: Context, permission: String): Boolean {
        val res = context.checkCallingOrSelfPermission(permission)
        return res == PackageManager.PERMISSION_GRANTED
    }

    @Throws(URISyntaxException::class)
    fun appendToUrl(url: URL, parameters: Map<String, String>): URL {
        if (parameters.isEmpty())
            return url

        val uri = url.toURI()
        val query = uri.query

        val params: List<String> = parameters.map { (k, v) -> "$k=$v" }

        val queryParams = (listOfNotNull(query) + params).joinToString("&")

        val newUrl = URI(uri.scheme, uri.authority, uri.path, queryParams, uri.fragment).toURL()
        return newUrl
    }
}
