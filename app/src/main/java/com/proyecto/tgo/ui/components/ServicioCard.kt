package com.proyecto.tgo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.proyecto.tgo.model.ServicioTuristico

@Composable
fun ServicioCard(
    servicio: ServicioTuristico,
    onConfirmarClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = servicio.tipoServicio,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Fecha: ${servicio.fecha} - ${servicio.horaPickup}")
            Text(text = "Pickup: ${servicio.puntoPickup}")
            Text(text = "Pasajeros: ${servicio.pasajerosCount}")
            Text(
                text = "Estado: ${servicio.estadoRespuesta}",
                color = if (servicio.estadoRespuesta == "CONFIRMADO") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(8.dp))
            if (servicio.estadoRespuesta == "PENDIENTE") {
                Button(
                    onClick = { onConfirmarClick(servicio.id) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Confirmar Asignación")
                }
            }
        }
    }
}