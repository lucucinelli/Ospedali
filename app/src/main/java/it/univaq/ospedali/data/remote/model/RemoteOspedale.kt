package it.univaq.ospedali.data.remote.model

// data class autogenerata dal plugin JsonToKotlinClass inserendo un generico oggetto JSON
data class RemoteOspedale(
    val canno_inserimento: String,
    val ccomune: String,
    val cdata_e_ora_inserimento: String,
    val cidentificatore_in_openstreetmap: String,
    val clatitudine: String,
    val clongitudine: String,
    val cnome: String,
    val cprovincia: String,
    val cregione: String
)