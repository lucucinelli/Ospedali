package it.univaq.ospedali.domain.repository

import it.univaq.ospedali.domain.model.Ospedale

// interfaccia che permette ai livelli superiori dell'app l'accesso al data source remoto (json)
// centralizza i metodi per la modifica dei dati
// contiene la logica di business
interface OspedaleRemoteRepository {

    // creo una funzione che mi restiuisca una lista di oggetti della classe Ospedale
    suspend fun getOspedali(): List<Ospedale>
}