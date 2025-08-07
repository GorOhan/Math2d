plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.mathgame.android.feature)
    alias(libs.plugins.mathgame.android.library.compose)
}

android {
    namespace = "com.ohanyan.mathgame.onboarding"
}