plugins {
    alias(libs.plugins.mathgame.android.library)
    alias(libs.plugins.mathgame.android.library.compose)
    alias(libs.plugins.mathgame.hilt)
}

android {
    namespace = "com.ohanyan.mathgame.common"
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
}