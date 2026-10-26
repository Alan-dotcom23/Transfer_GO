package com.proyecto.tgo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.proyecto.tgo.ui.components.SelectorFecha
import com.proyecto.tgo.ui.components.SelectorHora
import com.proyecto.tgo.ui.components.formatearFecha
import com.proyecto.tgo.ui.components.formatearHora

@Composable
fun DisponibilidadScreen(modifier: Modifier = Modifier) {
    // Estados para controlar si los diálogos están visibles o no
    var mostrarDialogoFecha by remember { mutableStateOf(false) }
    var mostrarDialogoHora by remember { mutableStateOf(false) }

    // Estados para guardar los valores seleccionados
    var fechaSeleccionada by remember { mutableStateOf<Long?>(null) }
    var horaSeleccionada by remember { mutableStateOf("Seleccionar hora") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mi Disponibilidad",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp, top = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Configurar Horario de Viaje",
                    style = MaterialTheme.typography.titleMedium
                )

                // Botón para abrir el selector de fecha
                OutlinedButton(
                    onClick = { mostrarDialogoFecha = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Fecha: ${formatearFecha(fechaSeleccionada)}")
                }

                // Botón para abrir el selector de hora
                OutlinedButton(
                    onClick = { mostrarDialogoHora = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Hora: $horaSeleccionada")
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botón para guardar (por ahora solo visual)
        Button(
            onClick = { /* TODO: Guardar en base de datos */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Guardar Disponibilidad")
        }
    }

    // Lógica para mostrar los diálogos flotantes
    if (mostrarDialogoFecha) {
        SelectorFecha(
            onFechaSeleccionada = { fechaSeleccionada = it },
            onCerrar = { mostrarDialogoFecha = false }
        )
    }

    if (mostrarDialogoHora) {
        SelectorHora(
            onHoraSeleccionada = { hora, minuto ->
                horaSeleccionada = formatearHora(hora, minuto)
            },
            onCerrar = { mostrarDialogoHora = false }
        )
    }
}