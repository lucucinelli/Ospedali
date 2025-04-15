package it.univaq.ospedali.common

import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationListener



class LocationHelper (
    private val context: Context,
    private val onLocationChanged: (Location) -> Unit
) {

    private val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

    private val listener = LocationListener { location ->
        onLocationChanged(location)
    }

    fun start(){
        val isGPSEnabled = manager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
        val isNetworkEnabled = manager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ?: false

        val isFineGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val isCoarseGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        //Non facciamo tutto nello stesso if per evitare che ci siano problemi di confusione di update
        if(isFineGranted && isGPSEnabled){
            manager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10f, listener)
        } else if(isCoarseGranted && isNetworkEnabled){
            manager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10f, listener)
        }
    }

    fun stop(){
        manager?.removeUpdates(listener)
    }
}