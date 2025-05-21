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


data class MapUiState(
    val ospedali: List<Ospedale> = emptyList(),
    val loadingMsg: String? = null,
    val error: String? = null,
    val markerState: MarkerState? = null,
    val filteredOspedali: List<Ospedale> = emptyList(),
    val cameraPositionState: CameraPositionState = CameraPositionState(
        position = CameraPosition( LatLng(0.0, 0.0), 10f, 0f, 0f))
)

sealed class MapEvent{
    data object StartLocation: MapEvent()
    data object StopLocation: MapEvent()
}

@HiltViewModel
class MapViewModel @Inject constructor(
    // In questo modo creo un contesto globale che persista finché l'applicazione è in esecuzione
    @ApplicationContext private val context: Context,
    private val getOspedaliUseCase: GetOspedaliUseCase
) : ViewModel() {

    var uiState by mutableStateOf(MapUiState())
        private set

    // rendo la variabile distance osservabile
    var distance by mutableFloatStateOf(60000f)
        private set

    private val locationHelper = LocationHelper(context = context){ location ->

        val markerState = MarkerState( position = LatLng(location.latitude,location.longitude))

        val cameraPosition = CameraPosition(
            LatLng(location.latitude, location.longitude),
            13f,
            0f,
            0f
        )

        val filteredOspedali = uiState.ospedali.filter {
            val ospedaleLocation = Location("ospedale")
                .apply { latitude = it.latitudine
                    longitude = it.longitudine }
            location.distanceTo(ospedaleLocation) <= distance   // richiamo la variabile
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

    init {
        getOspedali()
    }

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
        viewModelScope.launch {
            getOspedaliUseCase().collect{ resurce ->
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