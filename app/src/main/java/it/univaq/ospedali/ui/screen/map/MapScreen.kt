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
        var contaOspedali = uiState.filteredOspedali.size;
        Column (modifier = Modifier){
            Text(   text = "Sono stati trovati $contaOspedali ospedali",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding( 10.dp)
                    .fillMaxWidth(),)
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