package it.univaq.ospedali.ui.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

// rappresenta permessi generici
// title e message sono usati nell aUI per il dialog
// permissions contiene i permessi specifici
sealed class Permission(
    val title: String,
    val message: String,
    val permissions: List<String>
)

// rappresenta i permessi di posizione
class LocationPermission: Permission(
    title = "Permessi di localizzazione",
    message = "Questa app ha bisogno di accedere alla tua posizione per mostrarti gli ospedali vicino a te",
    permissions = listOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

)

// in base all'evento del ciclo di vita che viene catturato esegue una specifica funzione lambda
class LifecycleEvent(
    val event: Lifecycle.Event,
    val action: () -> Unit = {}
)

// funzione composable che gestisce la logica dei permessi ed utilizza codice sperimentale
// controlla se un permesso è concesso ed esegue il content in caso affermativo
// altrimenti richiede il permesso o mostra un dialogo
// gestisce eventi di ciclo di vita
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionChecker(
    permission: Permission,
    events: List<LifecycleEvent> = emptyList(),
    content: @Composable () -> Unit = {} // funzione lanciata quando il permesso è concesso
){

    // crea un oggetto che tiene traccia dello stato dei permessi che gli vengono passati dalla classe LocalPermission
    val permissionState = rememberMultiplePermissionsState(
        permissions = permission.permissions
    )
    if(permissionState.allPermissionsGranted){
        content()  // esegue la funzione lambda specificata nel costruttore

        // se si verificano nel mentre eventi, per ciascun evento esegui la corrispondente funzione lambda
        events.forEach{
            LifecycleEventEffect(it.event) {
                it.action()
            }
        }
    } else {  // se i permessi non sono stati concessi
        if(permissionState.shouldShowRationale) {  // se l'utente ha già negato una volta, allora si ricorre alla rationale
            PermissionDialog(
                permission = permission,
                onDismiss = {  },
                onRequest = { permissionState.launchMultiplePermissionRequest() }
            )
        }
        else{  // se invece è la prima volta che accede all'app o non è necessario mostrare la rationale
            SideEffect{
                permissionState.launchMultiplePermissionRequest()  // chiede direttamente i permessi
            }

        }
    }
}

// usa codice sperimentale
// mostra un dialogo con i permessi specificati
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun PermissionDialog(
    permission: Permission = LocationPermission(),
    onDismiss: () -> Unit = {},
    onRequest: () -> Unit = {}
){
    // finestra di dialogo per la richiesta dei permessi
    BasicAlertDialog(
        onDismissRequest = onDismiss,
    ){
        Column (
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = permission.title,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = permission.message,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )

            Row (
                modifier = Modifier.padding(top = 16.dp)
            ){
                OutlinedButton(onClick = onDismiss) {
                    Text(text = "Chiudi")  // chiude il dialogo
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = onRequest ) {
                    Text(text = "Richiedi")  // chiede di nuovo i permessi
                }
            }
        }
    }
}