package com.example.fastweather
import retrofit2.http.GET
import retrofit2.http.Query
interface WeatherAPIService {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") city: String
    ): WeatherResponse
}