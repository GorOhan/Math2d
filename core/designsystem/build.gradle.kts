plugins {
    alias(libs.plugins.mathgame.android.library)
    alias(libs.plugins.mathgame.android.library.compose)
}

android {
    namespace = "com.ohanyan.mathgame.designsystem"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    api(libs.androidx.compose.foundation)
}