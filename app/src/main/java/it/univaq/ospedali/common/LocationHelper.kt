package it.univaq.ospedali.common

import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationListener


// permette di ricevere aggiornamenti sulla posizione dell'utente
class LocationHelper (
    private val context: Context, // permette di accedere ai servizi Android (es. GPS)
    private val onLocationChanged: (Location) -> Unit  // funzione lambda che viene chiamata ogni volta che viene ricevuta una nuova posizione
) {

    // ottengo il location manager che serve per richiedere aggiornamenti di posizione da GPS o rete
    private val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

    // listener che riceve la nuova posizione
    private val listener = LocationListener { location ->
        onLocationChanged(location) // passa la nuova posizione alla funzione lambda
    }

    fun start(){
        // recupera informazioni di connettività del dispositivo
        val isGPSEnabled = manager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
        val isNetworkEnabled = manager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ?: false

        // recupera l'esito dei permessi chiesti all'utente
        val isFineGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val isCoarseGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        //Non facciamo tutto nello stesso if per evitare che ci siano problemi nell'update
        if(isFineGranted && isGPSEnabled){
            manager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10f, listener)
        } else if(isCoarseGranted && isNetworkEnabled){
            manager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10f, listener)
        }
    }

    // ferma il tracking per risparmio di batteria ed evitare memory leak
    fun stop(){
        manager?.removeUpdates(listener)
    }
}