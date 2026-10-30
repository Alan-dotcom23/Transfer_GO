package com.proyecto.tgo.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class MenuSeccion(val titulo: String, val icono: ImageVector) {
    SERVICIOS("Mis Servicios", Icons.Default.DirectionsCar),
    MAPA("Mapa", Icons.Default.Map),
    DISPONIBILIDAD("Disponibilidad", Icons.Default.EventAvailable),
    PERFIL("Mi Perfil", Icons.Default.Person)
}
