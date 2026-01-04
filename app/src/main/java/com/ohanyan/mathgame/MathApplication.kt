package com.ohanyan.mathgame

import android.app.Application
import com.ohanyan.ui.util.setAppLanguageBasedOnCountry
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MathApplication : Application() {
    override fun onCreate() {
        super.onCreate()
         setAppLanguageBasedOnCountry(this)
    }
}
