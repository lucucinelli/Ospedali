package it.univaq.ospedali.common

// BASE_URL serve a definire Retrofit, viene utilizzato per buildare gli oggetti kotlin tramite GsonConverterFactory
// indica l'url del server (file JSON) dal quale prendere le informazioni da convertire in oggetti koltin
const val BASE_URL = "https://raw.githubusercontent.com/AndrewCostant/MobileOspedali/refs/heads/main/"
// rappresenta il nome del file json da sottoporre al converter
const val API_DATA = "ospedali.json"
