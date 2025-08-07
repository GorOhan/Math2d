plugins {
    alias(libs.plugins.mathgame.android.feature)
    alias(libs.plugins.mathgame.android.library.compose)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.ohanyan.mathgame.playground"
}
dependencies {
    implementation("com.google.mlkit:digital-ink-recognition:18.1.0")
}
