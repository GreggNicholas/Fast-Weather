package com.example.fastweather

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WeatherViewModel(private val context: Context) : ViewModel() { // <-- context is passed in
    private val _recentCities = mutableStateListOf<String>()
    private val prefs = context.getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
    val recentCities: SnapshotStateList<String> get() = _recentCities

    var weather by mutableStateOf<WeatherResponse?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)

    init {
        val cities = prefs.getStringSet("recent_cities", emptySet()) ?: emptySet()
        _recentCities.addAll(cities)
    }

    private fun saveCities() {
        prefs.edit().putStringSet("recent_cities", _recentCities.toSet()).apply()
    }

    fun fetchWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.weatherAPIService.getCurrentWeather(apiKey, city)
                weather = response
                errorMessage = null
                visitCity(city) // successful fetch, add city
            } catch (e: Exception) {
                errorMessage = "Please enter a valid location"
            }
        }
    }

    fun getWeatherForCity(city: String, apiKey: String) {
        fetchWeather(city, apiKey)
    }

    private fun visitCity(city: String) {
        _recentCities.remove(city)
        _recentCities.add(0, city)
        if (_recentCities.size > 3) {
            _recentCities.removeAt(_recentCities.size - 1)
        }
        saveCities()
    }
}
