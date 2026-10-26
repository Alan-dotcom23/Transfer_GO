package com.proyecto.tgo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.proyecto.tgo.network.ClimaResponse
import com.proyecto.tgo.network.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun ClimaCard(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    var climaInfo by remember { mutableStateOf<ClimaResponse?>(null) }
    var error by remember { mutableStateOf(false) }

    // Reemplaza esto con tu API Key de OpenWeatherMap
    val apiKey = "ee99c439727feaa4f00a82ebcbfbf3b9"

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                climaInfo = RetrofitClient.apiService.obtenerClima(apiKey = apiKey)
            } catch (e: Exception) {
                error = true
            }
        }
    }

    Card(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (error) {
                Text("Error al cargar clima")
            } else if (climaInfo == null) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Column {
                    Text(
                        text = climaInfo!!.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${climaInfo!!.main.temp.toInt()}°C - ${climaInfo!!.weather.first().description.replaceFirstChar { it.uppercase() }}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Carga el ícono oficial de OpenWeatherMap
                val iconCode = climaInfo!!.weather.first().icon
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/$iconCode@2x.png",
                    contentDescription = "Ícono del clima",
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }
}