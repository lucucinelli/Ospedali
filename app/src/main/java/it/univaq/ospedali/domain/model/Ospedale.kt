package it.univaq.ospedali.domain.model

// inseriamo gli attributi che ci interessa vedere con la user interface (alcuni provengono dal remote ospedale)

data class Ospedale (
    val id: Int?,

    val nome: String,
    val comune: String,
    val provincia: String,
    val regione: String,
    val latitudine: Double,
    val longitudine: Double,
)  {

}