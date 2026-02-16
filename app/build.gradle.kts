plugins {
    alias(libs.plugins.mathgame.android.application)
    alias(libs.plugins.mathgame.android.application.compose)
    alias(libs.plugins.mathgame.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.mathgame.android.application.firebase)
}

android {
    namespace = "com.ohanyan.mathgame"

    defaultConfig {
        applicationId = "com.ohanyan.mathgame"
        versionCode = 4
        versionName = "1.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders["firebaseAnalyticsEnabled"] = "true"
        }
        debug {
            manifestPlaceholders["firebaseAnalyticsEnabled"] = "false"
        }
    }

    bundle {
        language {
            enableSplit = false
        }
    }
}

dependencies {

    implementation(projects.core.ui)
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.feature.onboarding)
    implementation(projects.feature.playground)
    implementation(projects.feature.settings)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.billing.ktx)
}