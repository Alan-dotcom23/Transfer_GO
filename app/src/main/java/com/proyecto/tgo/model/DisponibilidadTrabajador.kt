package com.proyecto.tgo.model

import java.time.LocalDate

data class DisponibilidadTrabajador(
    val trabajadorId: String,
    val diasBloqueados: List<LocalDate> = emptyList()
)