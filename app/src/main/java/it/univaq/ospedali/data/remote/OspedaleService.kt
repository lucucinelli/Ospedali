package it.univaq.ospedali.data.remote

import it.univaq.ospedali.common.API_DATA
import it.univaq.ospedali.data.remote.model.RemoteOspedale
import retrofit2.http.GET

// effettua richieste HTTP e gestisce il risultato
interface OspedaleService {

    @GET(API_DATA) // dove API DATA corrisponde al nome del json che vogliamo scaricare
    // suspend perchè il download può essere sospeso temporaneamente senza bloccare il thread principale
    suspend fun downloadData(): List<RemoteOspedale>
}