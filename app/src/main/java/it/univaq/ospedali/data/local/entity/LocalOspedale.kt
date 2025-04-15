package it.univaq.ospedali.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


// l'Ospedale che voglio salvare nel mio sistema

@Entity(tableName = "ospedali") // indica che è una classe che rappresenta una tabella nel database

data class LocalOspedale (
    @PrimaryKey // indica che è la chiave primaria della tabella
    val id: Int?,
    val nome: String,
    val comune: String,
    val provincia: String,
    val regione: String,
    val latitudine: Double,
    val longitudine: Double,
){
}

// ricorda, è preferibile rinominare gli attributi in camelcase con @ColumnInfo(name = "nuovo nome")