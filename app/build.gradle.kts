plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    //hilt
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
//    alias(libs.plugins.hilt.android)
//    id("kotlin-kapt")
//    google-services plugin
    id("com.google.gms.google-services")
    // Add the Crashlytics Gradle plugin
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.sam.healthsense"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sam.healthsense"
        minSdk = 34
        targetSdk = 35
        versionCode = 1
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
    buildToolsVersion = "35.0.0"
}

dependencies {

    //navigation
    implementation (libs.androidx.navigation.compose)
    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    //hilt-navigation
    implementation(libs.hilt.navigation.compose)

    implementation(libs.androidx.media3.common.ktx)
    //retrofit dep
    implementation(libs.converter.moshi)
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)

    // OkHttp for networking
//    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
//    implementation("com.squareup.okhttp3:okhttp:4.12.0")
//    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
//    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.0")

    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // DateTime handling
    implementation(libs.kotlinx.datetime)

    //the desugaring
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // DateTime handling
    implementation(libs.kotlinx.datetime)

    //Material 3 extended icons
    implementation(libs.androidx.compose.material.icons.extended.android)
    //Accompanist
    implementation("com.google.accompanist:accompanist-permissions:0.37.3")
//    implementation("androidx.core:core-ktx:1.13.1")

    //FirebaseBom
    implementation(platform(libs.firebase.bom))
    //Firebase Authentication


    //Firebase Auth
    //implementation(libs.firebase.authentication)
    implementation(libs.firebase.auth.ktx)
    //implementation(libs.firebase.auth)

    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.analytics.ktx)
    implementation(libs.firebase.storage.ktx)
    //Datastore dep
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.runtime.ktx.v282)


    //testing turbine
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)

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
}