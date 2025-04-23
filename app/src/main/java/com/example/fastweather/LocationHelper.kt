package com.example.fastweather


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import java.util.Locale

/** Fetch user's last known location & reverse geocodes to city.
Calls onResult(null) if no location or permissions found
 */
@Suppress("DEPRECATION")
fun getLastKnownCity(context: Context, onResult: (String?) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // Check for location permission
    if (ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        onResult(null)
        return
    }

    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->
            if (location == null) {
                onResult(null)
                return@addOnSuccessListener
            }

            // Use background thread for blocking geocoding call
            Thread {
                try {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses: List<Address>? = geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    )
                    // Safe call on nullable list
                    val city = addresses?.firstOrNull()?.locality
                    Handler(Looper.getMainLooper()).post {
                        onResult(city)
                    }
                } catch (e: Exception) {
                    Handler(Looper.getMainLooper()).post {
                        onResult(null)
                    }
                }
            }.start()
        }
        .addOnFailureListener {
            onResult(null)
        }
}