plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
}

android {
    android.buildFeatures.buildConfig = true
    namespace = "com.ahmedmostafa.currency"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ahmedmostafa.currency"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 34
        versionCode  = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"http://data.fixer.io/api/\"")
            buildConfigField("String", "API_ACCESS_KEY", "\"f8bf29ae0fa1a2f3363575bd103cd05d\"")

        }
        debug {
            buildConfigField("String", "BASE_URL", "\"http://data.fixer.io/api/\"")
            buildConfigField("String", "API_ACCESS_KEY", "\"f8bf29ae0fa1a2f3363575bd103cd05d\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}
kapt {
    correctErrorTypes = true
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Dependency Injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    //ViewModel
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // UI and Navigation
    implementation( libs.navigation.fragment.ktx)
    implementation( libs.androidx.navigation.ui.ktx)
    implementation( libs.material)
    //implementation("androidx.navigation:navigation-compose:2.6.0-alpha03")

    // Networking
    implementation( libs.retrofit2.retrofit)
    implementation( libs.converter.gson)
    implementation( libs.logging.interceptor)

    // Coroutines
    implementation (libs.kotlinx.coroutines.android)
}