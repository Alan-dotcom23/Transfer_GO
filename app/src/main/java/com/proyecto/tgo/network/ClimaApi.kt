package com.proyecto.tgo.network

import retrofit2.http.GET
import retrofit2.http.Query

interface ClimaApi {
    @GET("data/2.5/weather")
    suspend fun obtenerClima(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric", // Para grados Celsius
        @Query("lang") lang: String = "es"        // Para descripciones en español
    ): ClimaResponse // (Usa la clase que ya tengas definida para la respuesta)
}