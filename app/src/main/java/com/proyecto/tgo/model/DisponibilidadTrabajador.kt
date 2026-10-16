package com.proyecto.tgo.model

data class DisponibilidadTrabajador(
    val idTrabajador: String,
    val nombreTrabajador: String,
    val rol: String,
    val diasBloqueados: List<String> = emptyList()
)