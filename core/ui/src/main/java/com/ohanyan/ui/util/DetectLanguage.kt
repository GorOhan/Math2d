package com.ohanyan.ui.util

import android.content.Context
import android.telephony.TelephonyManager
import java.util.Locale

fun setAppLanguageBasedOnCountry(context: Context) {
    val country = getUserCountry(context)

    val newLocale = if (country == "am") Locale("hy") else Locale.getDefault()

    val config = context.resources.configuration
    config.setLocale(newLocale)

    @Suppress("DEPRECATION")
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}
fun getUserCountry(context: Context): String {
    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val simCountry = telephonyManager.simCountryIso
    val networkCountry = telephonyManager.networkCountryIso

    return when {
        simCountry?.isNotEmpty() == true -> simCountry.lowercase()
        networkCountry?.isNotEmpty() == true -> networkCountry.lowercase()
        else -> Locale.getDefault().country.lowercase()
    }
}