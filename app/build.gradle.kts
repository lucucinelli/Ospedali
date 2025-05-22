// Il file configura l’app Android con tutte le impostazioni di compilazione e versione
plugins {
    alias(libs.plugins.android.application) // per l'app Android
    alias(libs.plugins.kotlin.android)  // per usare kotlin
    alias(libs.plugins.kotlin.compose)  // per usare jetpack compose

    /* Plugin KSP */
    alias(libs.plugins.google.devtools.ksp) // permette di utilizzare ksp

    /* Plugin Hilt */
    alias(libs.plugins.dagger.hilt.android) // permette di utilizzare hilt per il DI

    /* Plugin Serialization */
    alias(libs.plugins.jetbrains.serialization) // permette la serializzazione Kotlin da JSON

    /* Secrets Alias plugin */
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")  // serve a gestire segreti (come le chiavi API)
}

android {
    namespace = "it.univaq.ospedali"  // package namespace
    compileSdk = 35     // versione SDK con la quale si compila l'app

    defaultConfig {
        applicationId = "it.univaq.ospedali"  //ID univoco dell'app
        minSdk = 24 // versione minima SDK supportata
        targetSdk = 35  // versione target SDK
        versionCode = 1     // versione app
        versionName = "1.0" // versione visibile all'utente

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
        sourceCompatibility = JavaVersion.VERSION_11  // compatibilità con java
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true  // abilità jetpack compose
    }
}

dependencies {
    // librerie base Android e Compose utilizzate
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

    // librerie specifiche per "argomento"

    /* Dependencies */

    /* Retrofit */
    implementation(libs.retrofit)       // client HTTP per richieste HTTP
    implementation(libs.converter.gson) // converter per JSON (da server a oggetti Kotlin)

    /* Dependency Injection */
    implementation(libs.androidx.hilt.navigation.compose) // permette di utilizzare hilt con compose
    implementation(libs.hilt.android) // permette di utilizzare hilt con compose
    ksp(libs.hilt.android.compiler) // permette di utilizzare hilt con compose

    /* Room Database */
    implementation(libs.androidx.room.runtime)  // runtime Room
    implementation(libs.androidx.room.ktx)  // estensioni kotlin per Room
    ksp(libs.androidx.room.compiler)

    /* Navigation Compose */
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json) // libreria per la serializzazione con kotlin Serialization

    /* ViewModel Compose */
    implementation(libs.androidx.lifecycle.viewmodel.compose)  // supporto view model in compose


    /* Google Map */
    implementation(libs.maps.compose)   // libreria compose per mappa Google

    /* RunTime Permission*/
    implementation(libs.accompanist.permissions)    // libreria per gestione permessi runtime in Compose
    implementation(libs.androidx.lifecycle.runtime.compose) // lifecycle compose

}
