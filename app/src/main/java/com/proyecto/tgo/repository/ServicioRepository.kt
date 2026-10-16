package com.proyecto.tgo.repository

import com.proyecto.tgo.model.ServicioTuristico

class ServicioRepository {

    private val listaServicios = mutableListOf(
        ServicioTuristico(
            id = "TRF-101",
            tipoServicio = "Transfer Aeropuerto -> Hotel Icon",
            fecha = "2026-09-28",
            horaPickup = "08:30 AM",
            puntoPickup = "Aeropuerto SCL - Salida Internacional",
            puntoDropoff = "Hotel Icon, Las Condes",
            pasajerosCount = 4,
            tarifaCLP = 45000,
            estadoRespuesta = "PENDIENTE",
            estadoEjecucion = "POR_INICIAR"
        ),
        ServicioTuristico(
            id = "TOUR-202",
            tipoServicio = "Tour Half Day - Viña del Mar",
            fecha = "2026-09-29",
            horaPickup = "09:00 AM",
            puntoPickup = "Hotel Mandarín Oriental",
            puntoDropoff = "Hotel Mandarín Oriental",
            pasajerosCount = 2,
            tarifaCLP = 75000,
            estadoRespuesta = "CONFIRMADO",
            estadoEjecucion = "POR_INICIAR"
        )
    )

    fun obtenerServicios(): List<ServicioTuristico> {
        return listaServicios.toList()
    }

    fun confirmarServicio(idServicio: String): Boolean {
        val index = listaServicios.indexOfFirst { it.id == idServicio }
        if (index != -1) {
            listaServicios[index] = listaServicios[index].copy(estadoRespuesta = "CONFIRMADO")
            return true
        }
        return false
    }
}