package it.univaq.ospedali.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


// fa in modo che la dataclass venga "convertita" in una tabella dal nome "ospedali"
@Entity(tableName = "ospedali")
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

// ricorda, puoi rinominare gli attributi con @ColumnInfo(name = "nuovonome")