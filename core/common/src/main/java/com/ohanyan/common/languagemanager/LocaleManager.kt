package com.ohanyan.common.languagemanager

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

object LocaleHelper {
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

    fun getCurrentAppLanguage(context: Context): String {
        val localeList = AppCompatDelegate.getApplicationLocales()
        return if (localeList.isEmpty) {
            Locale.getDefault().language // Fallback to system default
        } else {
            localeList.get(0)?.language ?: Locale.getDefault().language
        }
    }
}