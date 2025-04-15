package it.univaq.ospedali.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import it.univaq.ospedali.data.local.dao.OspedaleDao
import it.univaq.ospedali.data.local.entity.LocalOspedale

@Database(entities = [LocalOspedale::class], version = 1, exportSchema = false) // definisco il database, l'exportSchema evita l'errore che altrimenti darebbe l'ultima versione
abstract class OspedaleDatabase: RoomDatabase() {

    // noi non sappiamo come implementare un ospedaledao, lo sa room database tramite la configurazione del room databse nel dependency injection
    abstract fun getOspedaleDao(): OspedaleDao
}