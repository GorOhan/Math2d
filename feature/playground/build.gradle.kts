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
    implementation("org.tensorflow:tensorflow-lite:2.12.0")
    implementation("org.tensorflow:tensorflow-lite-gpu:2.12.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.3.1")
}
