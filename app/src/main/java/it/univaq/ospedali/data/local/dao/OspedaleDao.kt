package it.univaq.ospedali.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import it.univaq.ospedali.data.local.entity.LocalOspedale
import kotlinx.coroutines.flow.Flow

@Dao // metodi che servono per salvare, aggiornare, cancellare e cercare gli ospedali nel database
interface OspedaleDao {

    @Upsert // metodo che permette di inserire o, se presente, aggiornare un ospedale nel database
    suspend fun insert(ospedale: LocalOspedale)

    @Upsert // metodo che permette di inserire o, se presente, aggiornare un ospedale nel database
    suspend fun insert(ospedali: List<LocalOspedale>)

    @Query("SELECT * FROM ospedali ORDER BY id ASC") // query che permette di selezionare tutti gli ospedali presenti nel database
    fun getAll(): Flow<List<LocalOspedale>>  // utilizziamo flow al posto di suspend in questo modo ritorno un flow di una lista di local ospedale

    @Query("DELETE FROM ospedali")
    suspend fun deleteAll()

    @Query("SELECT * FROM ospedali WHERE comune = :comune AND provincia = :provincia AND regione = :regione ORDER BY comune, regione, provincia")
    fun getOspedaliByComune(comune: String, provincia: String, regione: String): Flow<List<LocalOspedale>>
}