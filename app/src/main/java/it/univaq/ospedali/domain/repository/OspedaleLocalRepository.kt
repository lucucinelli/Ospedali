package it.univaq.ospedali.domain.repository

import it.univaq.ospedali.domain.model.Ospedale
import kotlinx.coroutines.flow.Flow

// interfaccia per il recupero e per l'utilizzo del database
interface OspedaleLocalRepository {

    suspend fun insert(Ospedale: Ospedale)

    suspend fun insert(Ospedali: List<Ospedale>)

    suspend fun clearAll()

    fun getAll(): Flow<List<Ospedale>>

    fun getOspedaliByComune(comune: String, provincia: String, regione: String): Flow<List<Ospedale>>

}