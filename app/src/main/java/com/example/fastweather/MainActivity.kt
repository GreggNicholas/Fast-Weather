package com.example.fastweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fastweather.ui.theme.FastWeatherTheme

class MainActivity : ComponentActivity() {
    private val viewModel = WeatherViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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