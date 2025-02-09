plugins {
    alias(libs.plugins.mathgame.android.library)
    alias(libs.plugins.mathgame.android.library.compose)
}

android {
    namespace = "com.ohanyan.mathgame.ui"
}

dependencies {
    api(projects.core.designsystem)
}