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

sealed class Permission(
    val title: String,
    val message: String,
    val permissions: List<String>
)

class LocationPermission: Permission(
    title = "Location Permission",
    message = "This app needs access to your location to show you the closest hospital",
    permissions = listOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

)

class LifecycleEvent(
    val event: Lifecycle.Event,
    val action: () -> Unit = {}
)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionChecker(
    permission: Permission,
    events: List<LifecycleEvent> = emptyList(),
    content: @Composable () -> Unit = {}
){
    val permissionState = rememberMultiplePermissionsState(
        permissions = permission.permissions
    )
    if(permissionState.allPermissionsGranted){
        content()

        events.forEach{
            LifecycleEventEffect(it.event) {
                it.action()
            }
        }
    } else {
        if(permissionState.shouldShowRationale) {
            PermissionDialog(
                permission = permission,
                onDismiss = {  },
                onRequest = { permissionState.launchMultiplePermissionRequest() }
            )
        }
        else{
            SideEffect{ // applica i cambiamenti per effetto degli eventi
                permissionState.launchMultiplePermissionRequest()
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun PermissionDialog(
    permission: Permission = LocationPermission(),
    onDismiss: () -> Unit = {},
    onRequest: () -> Unit = {}
){
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
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = onRequest ) {
                    Text(text = "Request")
                }
            }
        }
    }
}