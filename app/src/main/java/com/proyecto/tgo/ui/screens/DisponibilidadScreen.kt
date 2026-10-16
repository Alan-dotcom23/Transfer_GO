package com.proyecto.tgo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.proyecto.tgo.ui.theme.TgoPrimary
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisponibilidadScreen(onSolicitarBloqueo: (String) -> Unit = {}) {
    val datePickerState = rememberDatePickerState()
    var fechaSeleccionadaTexto by remember { mutableStateOf("Ninguna fecha seleccionada") }

    // Formateador de fecha
    val formatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { millis ->
            val date = Date(millis)
            fechaSeleccionadaTexto = formatter.format(date)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Gestión de Disponibilidad",
            style = MaterialTheme.typography.titleLarge,
            color = TgoPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Seleccione una fecha para solicitar bloqueo (mínimo 7 días de anticipación):",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Calendario nativo Material 3
        DatePicker(
            state = datePickerState,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Fecha seleccionada: $fechaSeleccionadaTexto",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (datePickerState.selectedDateMillis != null) {
                            onSolicitarBloqueo(fechaSeleccionadaTexto)
                        }
                    },
                    enabled = datePickerState.selectedDateMillis != null,
                    colors = ButtonDefaults.buttonColors(containerColor = TgoPrimary)
                ) {
                    Text("Solicitar Bloqueo de Día")
                }
            }
        }
    }
}