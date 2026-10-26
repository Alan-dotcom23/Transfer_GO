package com.proyecto.tgo.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// 1. Estructura de los datos que recibimos
data class ClimaResponse(
    val main: MainData,
    val weather: List<WeatherData>,
    val name: String
)
data class MainData(val temp: Double)
data class WeatherData(val description: String, val icon: String)

// 2. Interfaz de la petición
interface ClimaApiService {
    @GET("weather")
    suspend fun obtenerClima(
        @Query("q") ciudad: String = "Puerto Montt,cl", // Cambia esto por tu ciudad base
        @Query("appid") apiKey: String,
        @Query("units") unidades: String = "metric",
        @Query("lang") idioma: String = "es"
    ): ClimaResponse
}

// 3. Cliente Retrofit
object RetrofitClient {
    val apiService: ClimaApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ClimaApiService::class.java)
    }
}