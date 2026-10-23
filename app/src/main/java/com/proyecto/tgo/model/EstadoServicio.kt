package com.proyecto.tgo.model

import androidx.compose.ui.graphics.Color

enum class EstadoServicio(val etiqueta: String) {
    PENDIENTE("Pendiente"),
    CONFIRMADO("Confirmado"),
    POR_INICIAR("Por Iniciar"),
    EN_CURSO("En Curso"),
    FINALIZADO("Finalizado"),
    RECHAZADO("Rechazado");

    fun colorContenedor(): Color = when (this) {
        PENDIENTE -> Color(0xFFFFF3CD)
        CONFIRMADO -> Color(0xFFD1E7DD)
        POR_INICIAR -> Color(0xFFCFF4FC)
        EN_CURSO -> Color(0xFFE2E3E5)
        FINALIZADO -> Color(0xFFD1E7DD)
        RECHAZADO -> Color(0xFFF8D7DA)
    }

    fun colorTexto(): Color = when (this) {
        PENDIENTE -> Color(0xFF664D03)
        CONFIRMADO -> Color(0xFF0F5132)
        POR_INICIAR -> Color(0xFF055160)
        EN_CURSO -> Color(0xFF41464B)
        FINALIZADO -> Color(0xFF0F5132)
        RECHAZADO -> Color(0xFF842029)
    }
}