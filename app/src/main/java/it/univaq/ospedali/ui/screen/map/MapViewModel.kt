package it.univaq.ospedali.ui.screen.map

import android.content.Context
import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MarkerState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import it.univaq.ospedali.common.LocationHelper
import it.univaq.ospedali.common.Resource
import it.univaq.ospedali.domain.model.Ospedale
import it.univaq.ospedali.domain.use_case.GetOspedaliUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

// mostra lo stato corrente degli oggetti dell'interfaccia
data class MapUiState(
    val ospedali: List<Ospedale> = emptyList(),
    val loadingMsg: String? = null,
    val error: String? = null,
    val markerState: MarkerState? = null,  // marker della posizione corrente
    val filteredOspedali: List<Ospedale> = emptyList(),
    val cameraPositionState: CameraPositionState = CameraPositionState(
        position = CameraPosition( LatLng(0.0, 0.0), 10f, 0f, 0f))
)   // dove la mappa è centrata

// eventi inviati al ViewModel per avviare o fermare la cattura della posizione
sealed class MapEvent{
    data object StartLocation: MapEvent()
    data object StopLocation: MapEvent()
}

@HiltViewModel // inietto le dipendenze
class MapViewModel @Inject constructor(
    // il context serve a creare il location helper che cattura la posizione dell'utente
    @ApplicationContext private val context: Context,
    private val getOspedaliUseCase: GetOspedaliUseCase  // recupero la lista degli ospedali
) : ViewModel() {

    // stato osservabile della UI, quando cambia la mappa si aggiorna
    // mutableStateOf rende osservabile quella variabile da Jetpack Compose
    // MapUiState è il valore iniziale, quindi lista vuota di ospedali
    // private set vuol dire che solo da dentro il ViewModel si può modificare uiState, da fuori lo si può solo leggere (es. UI)
    var uiState by mutableStateOf(MapUiState())
        private set  // implementa lo UDF

    // rendo la variabile distance osservabile
    var distance by mutableFloatStateOf(60000f)
        private set

    // definisco il locationHelper che quando arriva una nuova posizione
    private val locationHelper = LocationHelper(context = context){ location ->

        // crea un marker nella posizione attuale
        val markerState = MarkerState( position = LatLng(location.latitude,location.longitude))

        // sposta la camera della mappa lì
        val cameraPosition = CameraPosition(
            // si prende la lat. e long. della posizione rilevata dal location helper
            // centra la vista sulla posizione attuale dell'utente
            LatLng(location.latitude, location.longitude),
            13f,
            0f,
            0f
        )

        // filtra gli ospedali secondo la distanza specificata
        val filteredOspedali = uiState.ospedali.filter {
            val ospedaleLocation = Location("ospedale")  // creo un oggetto Location di Android, cioè una posizione geografica
                .apply { latitude = it.latitudine   // recupero langitudine e latitudine dell'oggetto iesimo
                    longitude = it.longitudine }
            // distanceTo calcola la differenza in metri tra la posizione attuale dell'utente e quella dell'ospedale iesimo
            location.distanceTo(ospedaleLocation) <= distance   // se la condizione è verificata l'ospedale viene incluso nella lista
        }

        // aggiorno la mia posizione
        uiState = uiState.copy(
            markerState = markerState,
            filteredOspedali = filteredOspedali,
            cameraPositionState = CameraPositionState(
                position = cameraPosition
            )
        )
    }


    // processo eseguito quando il ViewModel viene creato, cioè ogni volt ache avvio l'app quando mi sposto sul map screen
    // per tutta l'app viene instanziato una sola volta
    // quando chiudpo l'app e la riapro riparte l'init
    init {
        getOspedali()
    }

    // funzione che cattura gli eventi e avvia o ferma la cattura della posizione
    fun onEvent(event : MapEvent){
        when(event){
            is MapEvent.StartLocation ->{
                locationHelper.start()
            }
            is MapEvent.StopLocation ->{
                locationHelper.stop()
            }
        }
    }

    private fun getOspedali() {
        // il View Model Scope permette di eseguire la coroutine (nelle parentesi graffe) tramite il metodo launch
        // e la coroutine viene eseguita finchè il ViewModel è attivo (fino a chisura schermata)
        // la coroutine è codice eseguibile in parallelo all'app, non causa il bloccaggio
        viewModelScope.launch {
            getOspedaliUseCase().collect{ resurce ->  // restituisce uno stream di dati
                // in base all'esito dello useCase aggiorno lo uiState
                uiState = when(resurce){
                    is Resource.Loading -> {
                        uiState.copy(
                            loadingMsg = resurce.message,
                            error = null
                        )
                    }
                    is Resource.Success -> {
                        uiState.copy(
                            ospedali = resurce.data ?: emptyList(),
                            loadingMsg = null,
                            error = null
                        )
                    }
                    is Resource.Error ->{
                        uiState.copy(
                            loadingMsg = null,
                            error = resurce.message
                        )
                    }
                }

            }
        }
    }
}