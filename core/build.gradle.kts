plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.testforwork.core"
    compileSdk = 36

    defaultConfig {
        minSdk = 30
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Core модуль для общих компонентов (сервисы и т.д.)
    implementation(libs.androidx.core.ktx)
    // Для NotificationCompat
    implementation("androidx.core:core-ktx:1.13.1")
}

