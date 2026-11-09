package com.proyecto.tgo.network

import com.google.gson.annotations.SerializedName

data class ClimaResponse(
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("main") val main: Main
)

data class Weather(
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class Main(
    @SerializedName("temp") val temp: Double
)