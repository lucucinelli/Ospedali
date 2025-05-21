plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    /* Plugin KSP */
    alias(libs.plugins.google.devtools.ksp) // permette di utilizzare ksp

    /* Plugin Hilt */
    alias(libs.plugins.dagger.hilt.android) // permette di utilizzare hilt con compose

    /* Plugin Serialization */
    alias(libs.plugins.jetbrains.serialization) // permette di utilizzare serialization

    /* Secrets Alias plugin */
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "it.univaq.ospedali"
    compileSdk = 35

    defaultConfig {
        applicationId = "it.univaq.ospedali"
        minSdk = 24
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
    implementation(libs.volley)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    /* Dependencies */

    /* Retrofit */
    implementation(libs.retrofit)
    implementation(libs.converter.gson) // fa in modo che dal server arrivino dati in formato json

    /* Dependency Injection */
    implementation(libs.androidx.hilt.navigation.compose) // permette di utilizzare hilt con compose
    implementation(libs.hilt.android) // permette di utilizzare hilt con compose
    ksp(libs.hilt.android.compiler) // permette di utilizzare hilt con compose

    /* Room Database */
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx) // librerie in versione kotlin
    ksp(libs.androidx.room.compiler)

    /* Navigation Compose */
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json) // libreria per la serializzazione di kotlinx

    /* ViewModel Compose */
    implementation(libs.androidx.lifecycle.viewmodel.compose)


    /* Google Map */
    implementation(libs.maps.compose)

    /* RunTime Permission*/
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.lifecycle.runtime.compose)

}
