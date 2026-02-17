package com.ohanyan.common.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings

object Utils {

    fun shareApp(
        context: Context,
        title: String,
    ) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "$title https://play.google.com/store/apps/details?id=${context.packageName}"
            )
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    fun Context.openInternetSettingsSafely() {
        try {
            startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
        } catch (e: Exception) {
            try {
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            } catch (ignored: Exception) {
            }
        }
    }

}