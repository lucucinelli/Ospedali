package it.univaq.ospedali.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import it.univaq.ospedali.data.local.dao.OspedaleDao
import it.univaq.ospedali.data.local.entity.LocalOspedale

// funzione che prende in ingresso le entity da trasformare in tabelle del database
@Database(entities = [LocalOspedale::class], version = 1, exportSchema = false)
abstract class OspedaleDatabase: RoomDatabase() {

    // noi non sappiamo come implementare un ospedaledao
    // lo sa room database che sfrutta il dependency injection
    abstract fun getOspedaleDao(): OspedaleDao
}