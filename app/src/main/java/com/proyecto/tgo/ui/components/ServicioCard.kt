package com.proyecto.tgo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.proyecto.tgo.model.EstadoServicio
import com.proyecto.tgo.model.ServicioTuristico

@Composable
fun ServicioCard(
    servicio: ServicioTuristico,
    onConfirmar: () -> Unit,
    onRechazar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = servicio.clienteNombre,
                    style = MaterialTheme.typography.titleMedium
                )
                SuggestionChip(
                    onClick = { },
                    label = { Text(servicio.estado.etiqueta) },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = servicio.estado.colorContenedor(),
                        labelColor = servicio.estado.colorTexto()
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("📅 Fecha: ${servicio.fecha} · 🕒 ${servicio.horaPickup}")
            Text("📍 Pickup: ${servicio.puntoPickup}")
            Text("🏁 Dropoff: ${servicio.puntoDropoff}")
            Text(
                text = "💰 Tarifa: ${servicio.tarifaFormateada}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            servicio.observacion?.let { obs ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "📝 Obs: $obs",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (servicio.estado == EstadoServicio.PENDIENTE) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onRechazar,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Rechazar")
                    }
                    Button(
                        onClick = onConfirmar,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Confirmar")
                    }
                }
            }
        }
    }
}