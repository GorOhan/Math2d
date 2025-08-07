package com.ohanyan.common.utils

import android.content.Context
import android.content.Intent

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
}