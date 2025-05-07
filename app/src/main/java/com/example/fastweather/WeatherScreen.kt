package com.example.fastweather

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
    val keyboardController = LocalSoftwareKeyboardController.current
    var city by remember { mutableStateOf("") }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            getLastKnownCity(context) { detected ->
                detected?.let { city = it }
            }
        }
    }

    CompositionLocalProvider(LocalContentColor provides Color.White) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.Center)
            ) {
                // Background city image
                Image(
                    painter = painterResource(id = R.drawable.weathercity),
                    contentDescription = "Weather City Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp)
                )

                Text(
                    text = "Recent Cities",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Green,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                // Horizontal chip list for recent cities
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    items(viewModel.recentCities) { cityName ->
                        AssistChip(
                            onClick = {
                                city = cityName
                                viewModel.getWeatherForCity(cityName, apiKey)
                            },
                            label = {
                                Text(
                                    text = cityName,
                                    color = Color.Green,
                                    fontSize = 14.sp
                                )
                            },
                            modifier = Modifier.padding(horizontal = 1.dp)
                        )
                    }
                }

                // City input with enter key handling
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    singleLine = true,
                    placeholder = {
                        Text("Enter City", color = Color.LightGray, fontSize = 20.sp)
                    },
                    textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    trailingIcon = {
                        IconButton(onClick = {
                            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Use My Location",
                                tint = Color.White
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            if (city.isNotBlank()) {
                                viewModel.fetchWeather(city, apiKey)
                            }
                        }
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
                    modifier = Modifier.fillMaxWidth()
                )

                // Get Weather button (in case they prefer clicking)
                Button(
                    onClick = {
                        viewModel.fetchWeather(city, apiKey)
                    },
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text("Get Weather")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Weather results
                viewModel.weather?.let { weather ->
                    Text("Location: ${weather.location.name}, ${weather.location.country}")
                    Text("Temp: ${weather.current.temp_f}°F")
                    Text("Condition: ${weather.current.condition.text}")
                    Text("Humidity: ${weather.current.humidity}%")
                    Image(
                        painter = rememberAsyncImagePainter("https:${weather.current.condition.icon}"),
                        contentDescription = "Weather Icon",
                        modifier = Modifier.size(40.dp)
                    )
                }

                // Error handling
                viewModel.errorMessage?.let { error ->
                    Text("Error: $error", color = Color.Red)
                }
            }
        }
    }
}

