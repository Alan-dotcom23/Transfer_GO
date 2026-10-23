package com.proyecto.tgo.repository

import com.proyecto.tgo.model.EstadoServicio
import com.proyecto.tgo.model.ServicioTuristico
import java.time.LocalDate
import java.time.LocalTime

open class ServicioRepository {

    private val serviciosFake = mutableListOf(
        ServicioTuristico(
            id = "SERV-001",
            clienteNombre = "Hotel Muelle de las Almas",
            fecha = LocalDate.now().plusDays(2),
            horaPickup = LocalTime.of(8, 30),
            puntoPickup = "Centro de Castro",
            puntoDropoff = "Cucao",
            tarifaCLP = 45000,
            observacion = "Pasajeros requieren silla de bebé",
            estado = EstadoServicio.PENDIENTE
        ),
        ServicioTuristico(
            id = "SERV-002",
            clienteNombre = "Tour Saltos del Petrohué",
            fecha = LocalDate.now().plusDays(5),
            horaPickup = LocalTime.of(9, 0),
            puntoPickup = "Puerto Varas",
            puntoDropoff = "Volcán Osorno",
            tarifaCLP = 60000,
            estado = EstadoServicio.CONFIRMADO
        )
    )

    open suspend fun obtenerServicios(): List<ServicioTuristico> {
        return serviciosFake.toList()
    }

    open suspend fun actualizarEstado(id: String, nuevoEstado: EstadoServicio) {
        val index = serviciosFake.indexOfFirst { it.id == id }
        if (index != -1) {
            serviciosFake[index] = serviciosFake[index].copy(estado = nuevoEstado)
        }
    }
}