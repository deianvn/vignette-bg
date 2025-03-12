plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.10"
}

android {
    namespace = "com.github.deianvn.my.vignette"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.github.deianvn.my.vignette"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(17)
        }
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    val lifecycle = "2.8.7"
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycle")
    implementation("androidx.activity:activity-compose:1.10.0")

    val composebom = "2025.01.00"
    implementation(platform("androidx.compose:compose-bom:$composebom"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")

    // Koin for Android
    api("io.insert-koin:koin-core:4.0.2")
    api("io.insert-koin:koin-android:4.0.2")

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // RxJava
    implementation("io.reactivex.rxjava3:rxjava:3.1.10")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")

    // RxBindings
    implementation("com.jakewharton.rxbinding4:rxbinding:4.0.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.11.0")

    // Moshi
    implementation("com.squareup.moshi:moshi-kotlin:1.15.2")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.startup:startup-runtime:1.2.0")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.2")

    // Joda
    implementation("net.danlew:android.joda:2.13.0.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
