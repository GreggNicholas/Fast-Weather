package com.example.fastweather

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


class WeatherViewModel : ViewModel() {
    var weather by mutableStateOf<WeatherResponse?>(null)
        private set
    var errorMessage by mutableStateOf<String?>(null)

    fun fetchWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.weatherAPIService.getCurrentWeather(apiKey, city)
                weather = response
                errorMessage = null
            } catch (e: Exception) {
                errorMessage = "Could not fetch weather: &{e.message}"
            }
        }
    }
}