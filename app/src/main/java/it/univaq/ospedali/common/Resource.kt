package it.univaq.ospedali.common

sealed class Resource<T> {

    data class Success<T>(val data: T) : Resource<T>() // classe che contiene i dati in caso di successo

    data class Error<T>(val message: String) : Resource<T>() // classe che contiene il messaggio di errore se si è verificato un problema

    data class Loading<T>(val message: String?) : Resource<T>() // classe che restituisce un messaggio di caricamento che potrebbe anche essere nullo

}