package com.proyecto.tgo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.proyecto.tgo.model.ServicioTuristico

// Función auxiliar para deducir coordenadas según el texto del destino
fun obtenerCoordenadasPorDestino(destino: String): Pair<Double, Double> {
    return when {
        destino.contains("Aeropuerto", ignoreCase = true) || destino.contains("Tepual", ignoreCase = true) -> Pair(-41.4388, -73.0940)
        destino.contains("Puerto Varas", ignoreCase = true) -> Pair(-41.3195, -72.9854)
        destino.contains("Frutillar", ignoreCase = true) -> Pair(-41.1354, -73.0270)
        // Por defecto: Puerto Montt Centro
        else -> Pair(-41.4693, -72.9424)
    }
}

@Composable
fun ServicioCard(
    servicio: ServicioTuristico,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Calculamos las coordenadas del destino del viaje
    val destinoCoords = obtenerCoordenadasPorDestino(servicio.puntoDropoff)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Cliente y Estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = servicio.clienteNombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Badge(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                    Text(
                        text = servicio.estado.name,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Fecha y Hora
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("${servicio.fecha} - ${servicio.horaPickup}", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Ruta (Pickup -> Dropoff)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("${servicio.puntoPickup} ➔ ${servicio.puntoDropoff}", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // Fila Final: Clima a la izquierda, Tarifa a la derecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Aquí llamamos al clima con las coordenadas del destino
                ClimaCard(lat = destinoCoords.first, lon = destinoCoords.second)

                Text(
                    text = "Tarifa: ${servicio.tarifaFormateada}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}