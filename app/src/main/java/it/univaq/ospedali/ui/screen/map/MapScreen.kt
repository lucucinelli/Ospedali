package it.univaq.ospedali.ui.screen.map

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import it.univaq.ospedali.DetailActivity
import it.univaq.ospedali.ui.tools.LifecycleEvent
import it.univaq.ospedali.ui.tools.LocationPermission
import it.univaq.ospedali.ui.tools.PermissionChecker


@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel()  // dovendo utilizzare il view model che implementa il dependency injection devo utilizzare il metodo hiltViewModel
){

    val uiState = viewModel.uiState

    val context = LocalContext.current // serve per il metodo startActivity

    // verifica per i permessi
    PermissionChecker(
        permission = LocationPermission(),
        events = listOf(
            LifecycleEvent(Lifecycle.Event.ON_RESUME){  // app visibile
                viewModel.onEvent(MapEvent.StartLocation)   // inizia a catturare la posizione
            },
            LifecycleEvent(Lifecycle.Event.ON_PAUSE){  // app in background
                viewModel.onEvent(MapEvent.StopLocation)  // mette in pausa la cattura
            }
        )
    ){
        // riprendo la variabile dal view model avendola resa osservabile
        // definisco come val e non var dato che non è variabile
        val distance = viewModel.distance
        // converto in metri in km usando la funzione
        val distanceString = metriInKmString(distance)
        val contaOspedali = uiState.filteredOspedali.size
        Column (modifier = Modifier){
            Text(   text = "Sono stati trovati $contaOspedali ospedali",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding( 5.dp)
                    .fillMaxWidth()
            )
            Text(   text = "nel raggio di $distanceString",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(start = 5.dp, end = 5.dp, top = 2.dp)
                        .fillMaxWidth()

            )

            GoogleMap(
                modifier = modifier,
                cameraPositionState = uiState.cameraPositionState  // permette gesture nella mappa
            ){
                uiState.markerState?.let { // se esise un marker lo aggiungo alla mappa
                    Marker(
                        state = it,
                        title = "Current Location",
                        snippet = "You are here",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                    )
                }

                uiState.filteredOspedali.forEach{ ospedale ->

                    Marker(
                        //serve per specificare la posizione del marker
                        state = rememberMarkerState(position = LatLng(ospedale.latitudine, ospedale.longitudine)),
                        title = ospedale.id.toString(),
                        snippet = "${ospedale.regione}, ${ospedale.provincia}, ${ospedale.comune}",
                        onInfoWindowClick = {
                            context.startActivity(Intent(context, DetailActivity::class.java)
                                .apply {
                                    putExtra("comune", ospedale.comune)
                                    putExtra("provincia", ospedale.provincia)
                                    putExtra("regione", ospedale.regione)
                                })
                        }
                    )
                }
            }
        }


    }


}

// conversione distanza in km a 2 cifre decimali
fun metriInKmString(metri: Float): String {
    val km = metri / 1000f
    return String.format("%.2f km", km)
}