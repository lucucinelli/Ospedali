package it.univaq.ospedali.common

// può essere estesa da un numero finito di sottoclassi (sealed)
// gestisce lo stato di una richiesta o un'operazione asincrona (es. una query al db)
// è di tipo T quindi la classe lavora con un tipo generico (es. Lista di ospedali, singolo ospedale ecc)
sealed class Resource<T> {

    data class Success<T>(val data: T) : Resource<T>() // classe che contiene i dati ottenuti in caso di successo e come deve comportarsi la UI

    data class Error<T>(val message: String) : Resource<T>() // classe che contiene il messaggio di errore se si è verificato un problema

    data class Loading<T>(val message: String?) : Resource<T>() // classe che restituisce un messaggio di caricamento che potrebbe anche essere nullo
}