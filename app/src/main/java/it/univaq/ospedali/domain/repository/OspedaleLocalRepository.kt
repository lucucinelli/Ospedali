package it.univaq.ospedali.domain.repository

import it.univaq.ospedali.domain.model.Ospedale
import kotlinx.coroutines.flow.Flow

// interfaccia che permette ai livelli superiori dell'app l'accesso al data source locale
// centralizza i metodi per la modifica dei dati
// contiene la logica di business
interface OspedaleLocalRepository {

    suspend fun insert(Ospedale: Ospedale)

    suspend fun insert(Ospedali: List<Ospedale>)

    suspend fun clearAll()

    fun getAll(): Flow<List<Ospedale>>

    fun getOspedaliByComune(comune: String, provincia: String, regione: String): Flow<List<Ospedale>>

}