package com.proyecto.tgo.model

import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.Locale

data class ServicioTuristico(
    val id: String,
    val clienteNombre: String,
    val fecha: LocalDate,
    val horaPickup: LocalTime,
    val puntoPickup: String,
    val puntoDropoff: String,
    val tarifaCLP: Int,
    val observacion: String? = null,
    val estado: EstadoServicio = EstadoServicio.PENDIENTE
) {
    val tarifaFormateada: String
        get() {
            val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
            return format.format(tarifaCLP)
        }
}