package com.example.fastweather

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    apiKey: String,
) {
    val context = LocalContext.current
    var city by remember { mutableStateOf("") }

    // 1) Kick off location lookup once on first composition
    LaunchedEffect(Unit) {
        getLastKnownCity(context) { detectedCity ->
            detectedCity?.let { city = it }
        }
    }

    // 2) Provide a white content color, then draw your background + content
    CompositionLocalProvider(LocalContentColor provides Color.White) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // 3) All your UI goes *inside* the Box’s lambda
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center)
            ) {
                // City image
                Image(
                    painter = painterResource(id = R.drawable.weathercity),
                    contentDescription = "Weather City Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)       // lower height so it doesn’t push everything off
                )

                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    placeholder = { Text("Enter City", color = Color.LightGray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                        focusedPlaceholderColor = Color.LightGray,
                        unfocusedPlaceholderColor = Color.LightGray
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )

                Button(
                    onClick = { viewModel.fetchWeather(city, apiKey) },
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text("Get Weather")
                }

                Spacer(modifier = Modifier.height(16.dp))

                viewModel.weather?.let { weather ->
                    Text("Location: ${weather.location.name}, ${weather.location.country}")
                    Text("Temp: ${weather.current.temp_f}°F")
                    Text("Condition: ${weather.current.condition.text}")
                    Text("Humidity: ${weather.current.humidity}%")

                    Image(
                        painter = rememberAsyncImagePainter("https:${weather.current.condition.icon}"),
                        contentDescription = "Weather Icon",
                        modifier = Modifier
                            .size(64.dp)
                            .padding(top = 8.dp)
                    )
                }

                viewModel.errorMessage?.let { error ->
                    Text("Error: $error", color = Color.Red)
                }
            }
        }
    }
}
