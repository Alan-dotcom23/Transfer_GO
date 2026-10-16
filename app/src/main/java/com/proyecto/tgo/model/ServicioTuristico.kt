package com.proyecto.tgo.model

data class ServicioTuristico(
    val id: String,
    val tipoServicio: String,
    val fecha: String,
    val horaPickup: String,
    val puntoPickup: String,
    val puntoDropoff: String,
    val pasajerosCount: Int,
    val tarifaCLP: Int,
    val estadoRespuesta: String,
    val estadoEjecucion: String,
    val observacion: String? = null
)