package com.example.fastweather

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fastweather.ui.theme.FastWeatherTheme

class MainActivity : ComponentActivity() {
    private val viewModel = WeatherViewModel()

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

        val apiKey = "85494a9c85d34806b2705718250904"
        enableEdgeToEdge()
        setContent {
            WeatherScreen(viewModel = viewModel, apiKey = apiKey)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FastWeatherTheme {
        Greeting("Android")
    }
}