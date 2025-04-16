package com.example.fastweather

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter


@Composable
fun WeatherScreen(viewModel: WeatherViewModel, apiKey: String) {
    var city by remember { mutableStateOf("") }
    CompositionLocalProvider(LocalContentColor provides Color.White) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        )
        Column(modifier = Modifier.padding(16.dp)) {
            // Insert city image
            Image(
                painter = painterResource(id = R.drawable.weathercity),
                contentDescription = "Weather City Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
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
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { viewModel.fetchWeather(city, apiKey) },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Get Weather")
            }

            viewModel.weather?.let { weather ->
                Text("Location: ${weather.location.name}, ${weather.location.country}")
                Text("Temp: ${weather.current.temp_f}°F")
                Text("Condition: ${weather.current.condition.text}")
                Text("Humidity: ${weather.current.humidity}%")

                Image(
                    painter = rememberAsyncImagePainter("https:${weather.current.condition.icon}"),
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(64.dp)
                )
            }

            viewModel.errorMessage?.let { error ->
                Text("Error: $error", color = Color.Red)
            }
        }
    }
}