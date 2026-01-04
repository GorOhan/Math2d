package com.ohanyan.ui.util

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import android.telephony.TelephonyManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

fun setAppLanguageBasedOnCountry(context: Context) {
    val country = getUserCountry(context)

   //val newLocale = if (country == "am") Locale("hy") else Locale.getDefault()
   val newLocale = Locale("hy")

    setAppLanguage(context, newLocale.language)

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

fun setAppLanguage(context: Context, languageCode: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(LocaleManager::class.java)
            ?.applicationLocales = LocaleList.forLanguageTags(languageCode)
    } else {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(languageCode)
        )
    }
}