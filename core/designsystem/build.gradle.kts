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

    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3.adaptive)
    api(libs.androidx.compose.material3.navigationSuite)
    compileOnly(platform(libs.androidx.compose.bom))

}