package com.example.fastweather

data class WeatherResponse(
    val location: Location,
    val current: Current
)

data class Location(
    val name: String,
    val region: String,
    val country: String
)

data class Current(
    val temp_f: Double,
    val condition: Condition,
    val humidity: Int
)

data class Condition(
    val text: String,
    val icon: String
)