package it.univaq.ospedali

import android.app.Application // classe necessaria per il dependency injection
import dagger.hilt.android.HiltAndroidApp
// Questo file serve per inizializzare Hilt in tutta l’applicazione e
// rappresenta il punto d’ingresso del Dependency Injection Container.
// funziona a patto che nel manifest ci sia, nel tag application, android:name = ".Ospedali"
@HiltAndroidApp // permette di utilizzare hilt con compose
class Ospedali:  Application() {
}