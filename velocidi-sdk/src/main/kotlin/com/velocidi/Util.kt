package com.velocidi

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import org.json.JSONArray
import org.json.JSONObject

internal data class ApplicationInfo(
    val appName: String,
    val appVersion: String,
    val androidSDK: String,
    val device: String
)

internal object Util {
    /**
     * Obtains the necessary information to build the User-Agent(UA)
     *
     * @param context Android application context
     * @return ApplicationInfo with all the relevant information to build the UA
     */
    fun getApplicationInfo(context: Context): ApplicationInfo {
        val sdkVersion = Build.VERSION.SDK_INT.toString()
        val device = if (Build.MANUFACTURER != null && Build.MODEL != null) {
            Build.MANUFACTURER + " " + Build.MODEL
        } else {
            Build.DEVICE ?: ""
        }

        return try {
            val packageManager = context.packageManager
            val packageName = context.packageName

            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            val packageInfo = packageManager.getPackageInfo(packageName, 0)

            val appName = packageManager.getApplicationLabel(applicationInfo) ?: packageName
            val appVersion = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode.toString()
            } else {
                packageInfo.versionName
            }

            ApplicationInfo(appName.toString(), appVersion.toString(), sdkVersion, device)
        } catch (e: PackageManager.NameNotFoundException) {
            ApplicationInfo("Unknown app", "Unknown version", sdkVersion, device)
        }
    }

    fun buildUserAgent(appInfo: ApplicationInfo): String {
        val extraInfo =
            System.getProperty("http.agent") ?: "(Android ${appInfo.androidSDK}; ${appInfo.device})"

        return "${appInfo.appName}/${appInfo.appVersion} ${Constants.SDK_NAME}/${Constants.SDK_VERSION} $extraInfo"
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
    fun Uri.appendToUrl(parameters: Map<String, String>): Uri {
        if (parameters.isEmpty()) {
            return this
        }

        val builder = this.buildUpon()
        parameters.forEach { (k, v) -> builder.appendQueryParameter(k, v) }
        return builder.build()
    }

    fun JSONObject.toQueryParams(): Map<String, String> {
        fun toQueryParamsAux(elem: Any?, qs: MutableMap<String, String>, path: String) {
            when (elem) {
                is JSONObject ->
                    for (key in elem.keys()) {
                        val pathKey = if (path.isEmpty()) key else "[$key]"
                        toQueryParamsAux(elem[key], qs, path + pathKey)
                    }
                is JSONArray ->
                    for (idx in 0..elem.length()) {
                        toQueryParamsAux(elem.opt(idx), qs, "$path[$idx]")
                    }
                is Boolean, is Int, is Byte, is Char, is String, is Double, is Float, is Long, is Short ->
                    qs[path] = elem.toString()
                else -> {}
            }
        }

        val qs = mutableMapOf<String, String>()
        toQueryParamsAux(this, qs, "")

        return qs
    }
}
