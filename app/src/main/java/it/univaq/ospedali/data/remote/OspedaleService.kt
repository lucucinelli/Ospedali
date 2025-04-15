package it.univaq.ospedali.data.remote

import it.univaq.ospedali.common.API_DATA
import it.univaq.ospedali.data.remote.model.RemoteOspedale
import retrofit2.http.GET

interface OspedaleService {

    @GET(API_DATA) // dove API DATA corrisponde al nome del json che vogliamo scaricare
    suspend fun downloadData(): List<RemoteOspedale>
}