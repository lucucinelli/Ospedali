package it.univaq.ospedali.domain.repository

import it.univaq.ospedali.domain.model.Ospedale


interface OspedaleRemoteRepository {

    // creo una funzione che mi restiuisca una lista di oggetti della classe Ospedale

    suspend fun getOspedali(): List<Ospedale>
}