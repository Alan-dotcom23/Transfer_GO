package com.proyecto.tgo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

/**
 * Diálogo al presionar "Iniciar": permite comenzar el viaje de inmediato
 * o agendar fecha y hora (se programa un recordatorio con notificación).
 */
@Composable
fun IniciarViajeDialog(
    servicioId: String,
    onIniciarAhora: () -> Unit,
    onAgendar: (LocalDateTime) -> Unit,
    onCerrar: () -> Unit
) {
    var agendar by remember { mutableStateOf(false) }
    var fechaMillis by remember { mutableStateOf<Long?>(null) }
    var hora by remember { mutableStateOf<LocalTime?>(null) }
    var mostrarFecha by remember { mutableStateOf(false) }
    var mostrarHora by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text("Iniciar viaje $servicioId") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                OpcionRadio(
                    texto = "Iniciar ahora",
                    seleccionada = !agendar,
                    onClick = { agendar = false; error = null }
                )
                OpcionRadio(
                    texto = "Agendar horario",
                    seleccionada = agendar,
                    onClick = { agendar = true }
                )

                if (agendar) {
                    OutlinedButton(
                        onClick = { mostrarFecha = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null)
                        Text(
                            text = "  Fecha: ${formatearFecha(fechaMillis)}"
                        )
                    }
                    OutlinedButton(
                        onClick = { mostrarHora = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Schedule, contentDescription = null)
                        Text(
                            text = "  Hora: ${hora?.let { formatearHora(it.hour, it.minute) } ?: "Seleccionar hora"}"
                        )
                    }
                }

                error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (!agendar) {
                        onIniciarAhora()
                        return@TextButton
                    }
                    val millis = fechaMillis
                    val horaElegida = hora
                    if (millis == null || horaElegida == null) {
                        error = "Selecciona la fecha y la hora"
                        return@TextButton
                    }
                    val fecha = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
                    val momento = LocalDateTime.of(fecha, horaElegida)
                    if (!momento.isAfter(LocalDateTime.now())) {
                        error = "El horario debe ser posterior a la hora actual"
                        return@TextButton
                    }
                    onAgendar(momento)
                }
            ) {
                Text(if (agendar) "Agendar" else "Iniciar ahora")
            }
        },
        dismissButton = {
            TextButton(onClick = onCerrar) { Text("Cancelar") }
        }
    )

    if (mostrarFecha) {
        SelectorFecha(
            soloDesdeHoy = true,
            onFechaSeleccionada = { fechaMillis = it; error = null },
            onCerrar = { mostrarFecha = false }
        )
    }
    if (mostrarHora) {
        SelectorHora(
            onHoraSeleccionada = { h, m -> hora = LocalTime.of(h, m); error = null },
            onCerrar = { mostrarHora = false }
        )
    }
}

@Composable
private fun OpcionRadio(texto: String, seleccionada: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = seleccionada, onClick = onClick, role = Role.RadioButton),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = seleccionada, onClick = null)
        Text(text = texto, modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 8.dp))
    }
}
