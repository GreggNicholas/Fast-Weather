package com.example.fastweather

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class WeatherViewModel : ViewModel() {
    private val _recentCities = mutableStateListOf<String>()
    val recentCities: SnapshotStateList<String> get() = _recentCities
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
// for recent city chips
    fun getWeatherForCity(city: String, apiKey: String) {
        fetchWeather(city, apiKey)
    }

    private fun visitCity(city: String) {
        _recentCities.remove(city) // remove is redundant city
        _recentCities.add(0, city) // add city to front of list
        if (_recentCities.size > 3) {
            _recentCities.removeAt(_recentCities.size - 1)
        }
    }
}