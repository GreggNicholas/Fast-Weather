package com.example.fastweather

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object RetrofitClient {
    private const val BASE_URL = "https://api.weatherapi.com/v1/"


    val weatherAPIService: WeatherAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(WeatherAPIService::class.java)
    }
}
