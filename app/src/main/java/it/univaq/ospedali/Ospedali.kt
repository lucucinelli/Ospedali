package it.univaq.ospedali

import android.app.Application // classe necessaria per il dependency injection
import dagger.hilt.android.HiltAndroidApp
// Questà è la prima classe che viene inizializzata all'avvio e inizializza Hilt in tutta l’app.
// rappresenta il punto d’ingresso del Dependency Injection che vivrà per tutta la durata dell’app.
// funziona a patto che nel manifest ci sia il name = ".Ospedali"
// l'annotazione la si deve usare in una classe che estenda application per far funzionare bene il DI
@HiltAndroidApp // permette di utilizzare hilt con compose
class Ospedali:  Application() {
}