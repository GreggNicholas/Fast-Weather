package com.example.fastweather

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : ComponentActivity() {

    // Register a permission launcher
    private val requestLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            // react here if the user denies
            if (!granted) {
                // Optional: inform user that location-based auto-fill won’t work
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  Ask for ACCESS_FINE_LOCATION at startup
        requestLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        val viewModel = WeatherViewModel(context = this)
        val apiKey = "85494a9c85d34806b2705718250904"
        enableEdgeToEdge()
        setContent {
            WeatherScreen(viewModel = viewModel, apiKey = apiKey)
        }
    }
}