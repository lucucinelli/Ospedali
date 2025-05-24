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


// quando viene eseguita viene creato automaticamente il ViewModel (DI)
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel()
){

    // prelevo lo uiState dal viewModel
    val uiState = viewModel.uiState

    // prendo il contesto corrente per poter lanciare successivamente la Detail Activity
    val context = LocalContext.current // serve per il metodo startActivity

    // verifico i permessi
    // usando la classe enumerativa LifecycleEvent che deriva dalla classe Lifecycle
    // per classe enumerativa si intende una classe che può assumere solo specifici valori
    // es. Stagione.Primavera, Stagione.Estate, ecc..
    // gestisce gli eventi che si verificano durante il ciclo di vita della una componente Android
    PermissionChecker(
        permission = LocationPermission(), // classe in tools/permissions
        events = listOf(
            LifecycleEvent(Lifecycle.Event.ON_RESUME){  // app visibile (componente interattivo)
                viewModel.onEvent(MapEvent.StartLocation)   // inizia a catturare la posizione
            },
            LifecycleEvent(Lifecycle.Event.ON_PAUSE){  // app in background (il componente ha perso il focus)
                viewModel.onEvent(MapEvent.StopLocation)  // mette in pausa la cattura
            }
        )
    ){
        // riprendo la variabile dal view model avendola resa osservabile
        // definisco come val e non var dato che non è variabile
        val distance = viewModel.distance

        // converto in km usando la funzione
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

            // creazione mappa
            GoogleMap(
                modifier = modifier,
                cameraPositionState = uiState.cameraPositionState  // posizione della vista dello uiState
            ){
                uiState.markerState?.let { // se esise un marker esegui il codice dentro let
                    // it rappresenta il marker
                    Marker(
                        state = it, // imposto la posizione del marker
                        title = "Posizione corrente",
                        snippet = "Sei qui",   // sottotitolo
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                    )
                }

                uiState.filteredOspedali.forEach{ ospedale ->

                    Marker(
                        // remember perchè viene instanziato una sola volta
                        state = rememberMarkerState(position = LatLng(ospedale.latitudine, ospedale.longitudine)),
                        title = ospedale.id.toString(),
                        snippet = "${ospedale.regione}, ${ospedale.provincia}, ${ospedale.comune}",
                        onInfoWindowClick = {
                            // ::class è un oggetto KClass di Kotlin che fa riferimento ad una classe
                            // .java fa in modo che il KClass sia convertito in una classe Java
                            context.startActivity(Intent(context, DetailActivity::class.java)
                                .apply {    // passaggio valori dalla MapScreen alla DetailScreen
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