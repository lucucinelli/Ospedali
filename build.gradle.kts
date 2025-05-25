// Top-level build file where you can add configuration options common to all sub-projects/modules.
// plugin che si possono utilizzare nei moduli, ma non vengono applicati subito (apply false significa che il plugin è disponibile, ma non attivo in questo progetto root)
// Si utilizzano alias, quindi queste dipendenze sono definite in un file libs.versions.toml
plugins {
    alias(libs.plugins.android.application) apply false // plugin per l'applicazione
    alias(libs.plugins.kotlin.android) apply false  // per supporto Kotlin
    alias(libs.plugins.kotlin.compose) apply false  // per abilitare Jetpack Compose

    /* Plugin KSP */
    alias(libs.plugins.google.devtools.ksp) apply false // permette di utilizzare ksp così da leggere annotazioni compile time (es. @Dao, @Query..) ed eseguirle

    /* Plugin Hilt */
    alias(libs.plugins.dagger.hilt.android) apply false // permette di utilizzare hilt (dependency injection)

    /* Plugin Serialization */
    alias(libs.plugins.jetbrains.serialization) apply false // corrisponde al plugin kotlinx-serialization, abilita @Serializable (da file JSON in Kotlin)
}

buildscript{  // permette di definire tutti gli strumenti da passare al Gradle per la creazione dell'app
    dependencies{
        classpath(libs.secrets.gradle.plugin)   // indica quali librerie o plugin eseguire per costruire l'app
    }
}