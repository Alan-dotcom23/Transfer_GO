package com.proyecto.tgo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.TimeZone
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorFecha(
    onFechaSeleccionada: (Long?) -> Unit,
    onCerrar: () -> Unit,
    soloDesdeHoy: Boolean = false
) {
    // El DatePicker trabaja en milisegundos UTC a medianoche
    val inicioHoyUtc = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    val state = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                !soloDesdeHoy || utcTimeMillis >= inicioHoyUtc
        }
    )

    DatePickerDialog(
        onDismissRequest = onCerrar,
        confirmButton = {
            TextButton(
                onClick = {
                    onFechaSeleccionada(state.selectedDateMillis)
                    onCerrar()
                }
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = onCerrar) {
                Text("Cancelar")
            }
        }
    ) {
        DatePicker(state = state)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorHora(
    onHoraSeleccionada: (Int, Int) -> Unit,
    onCerrar: () -> Unit
) {
    val calendar = Calendar.getInstance()
    val state = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE)
    )

    AlertDialog(
        onDismissRequest = onCerrar,
        confirmButton = {
            TextButton(
                onClick = {
                    onHoraSeleccionada(state.hour, state.minute)
                    onCerrar()
                }
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = onCerrar) {
                Text("Cancelar")
            }
        },
        text = {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimePicker(state = state)
            }
        }
    )
}

// Funciones de ayuda para formatear los textos en pantalla
fun formatearFecha(millis: Long?): String {
    if (millis == null) return "Seleccionar fecha"
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    // El DatePicker entrega la fecha en UTC; sin esto se mostraba un día antes en Chile
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(Date(millis))
}

fun formatearHora(hora: Int, minuto: Int): String {
    return String.format(Locale.getDefault(), "%02d:%02d", hora, minuto)
}