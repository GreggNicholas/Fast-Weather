package com.example.fastweather

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    apiKey: String,
) {
    val context = LocalContext.current
    var city by remember { mutableStateOf("") }

    // prepare a launcher in Compose
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            // fetch the location and write into `city`
            getLastKnownCity(context) { detected ->
                detected?.let { city = it }
            }
        }
    }
    // Kick off location lookup once on first composition
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
    viewModel.recentCities.forEach { city ->
        Button(
            onClick = { viewModel.getWeatherForCity(city, apiKey) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text(text = city)
        }
    }

    //  Provide a white content color, then draw your background + content
    CompositionLocalProvider(LocalContentColor provides Color.White) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            //  All UI goes inside the Box’s lambda
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.Center)
            )
            {
                // City image
                Image(
                    painter = painterResource(id = R.drawable.weathercity),
                    contentDescription = "Weather City Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(550.dp)       // lower height so it doesn’t push everything off
                )
                Text(
                    text = "Recent Cities",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Green,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    items(viewModel.recentCities) { cityName ->
                        AssistChip(
                            onClick = { viewModel.getWeatherForCity(cityName, apiKey) },
                            label = { Text(text = cityName, color = Color.Green, fontSize = 14.sp) },
                            modifier = Modifier.padding(horizontal = 1.dp)

                        )
                    }
                }
                OutlinedTextField(
                    value = city, //autofill based on location permissions
                    onValueChange = { city = it },  // user can still edit autofill
                    placeholder = {
                        Text(
                            "Enter City",
                            color = Color.LightGray,
                            fontSize = 20.sp
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold // <-- This makes the typed text bold
                    ),

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
                        .padding(top = 2.dp)
                )

                Button(
                    onClick = { viewModel.fetchWeather(city, apiKey) },
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .fillMaxWidth()
                ) {
                    Text("Get Weather")
                }

                Spacer(modifier = Modifier.height(8.dp))

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
