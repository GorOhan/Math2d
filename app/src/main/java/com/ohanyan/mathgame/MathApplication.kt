package com.ohanyan.mathgame

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MathApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
