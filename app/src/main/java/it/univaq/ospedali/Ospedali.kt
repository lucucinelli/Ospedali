package it.univaq.ospedali

import android.app.Application // classe necessaria per il dependency injection
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp // permette di utilizzare hilt con compose
class Ospedali:  Application() {
}