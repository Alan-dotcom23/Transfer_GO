package com.proyecto.tgo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.proyecto.tgo.BuildConfig
import com.proyecto.tgo.network.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun ClimaCard(lat: Double, lon: Double, modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    // Cambia "ClimaResponse" por el nombre de tu clase de modelo del clima si es diferente
    var climaInfo by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf(false) }

    val apiKey = BuildConfig.OPENWEATHER_API_KEY

    LaunchedEffect(lat, lon) {
        coroutineScope.launch {
            try {
                val response = RetrofitClient.apiService.obtenerClima(lat, lon, apiKey)
                // Asumiendo que tu JSON parsea la temperatura y la descripción
                val temp = response.main.temp.toInt()
                val desc = response.weather.firstOrNull()?.description ?: ""
                climaInfo = "$temp°C - ${desc.replaceFirstChar { it.uppercase() }}"
                error = false
            } catch (e: Exception) {
                error = true
            }
        }
    }

    if (error) {
        Text("No se pudo cargar el clima", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
    } else {
        climaInfo?.let { clima ->
            Text(
                text = "🌤 Clima en destino: $clima",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        } ?: CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
    }
}