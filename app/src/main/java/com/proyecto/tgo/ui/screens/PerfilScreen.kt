package com.proyecto.tgo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

// Modelo para los perfiles de ejemplo
data class PerfilUsuarioEjemplo(
    val nombre: String,
    val rol: String,
    val agencia: String,
    val rut: String,
    val telefono: String,
    val calificacion: String,
    val detalleExtraTitulo: String,
    val detalleExtraValor: String,
    val iconoRol: ImageVector
)

@Composable
fun PerfilScreen() {
    // Lista de usuarios de ejemplo
    val listaUsuarios = remember {
        listOf(
            PerfilUsuarioEjemplo(
                nombre = "Alan Mancilla",
                rol = "Driver / Conductor Senior",
                agencia = "Southbound Tours",
                rut = "19.876.543-2",
                telefono = "+56 9 1234 5678",
                calificacion = "4.9 ⭐ (84 servicios)",
                detalleExtraTitulo = "Licencia de Conducir",
                detalleExtraValor = "Clase A2 / A3 Profesional",
                iconoRol = Icons.Default.DirectionsCar
            ),
            PerfilUsuarioEjemplo(
                nombre = "Luis Figueroa",
                rol = "Guía Turístico Bilingüe",
                agencia = "Turismo Patagonia Express",
                rut = "18.654.321-K",
                telefono = "+56 9 8765 4321",
                calificacion = "5.0 ⭐ (112 tours)",
                detalleExtraTitulo = "Idiomas Certificados",
                detalleExtraValor = "Español, Inglés (C1), Portugués",
                iconoRol = Icons.Default.Translate
            ),
            PerfilUsuarioEjemplo(
                nombre = "Carolina Reyes",
                rol = "Coordinadora de Terreno",
                agencia = "TGO Operaciones Central",
                rut = "17.432.109-8",
                telefono = "+56 9 5555 9988",
                calificacion = "4.8 ⭐ (Supervisora)",
                detalleExtraTitulo = "Zona de Asignación",
                detalleExtraValor = "Región Metropolitana / Valparaíso",
                iconoRol = Icons.Default.SupervisorAccount
            )
        )
    }

    var usuarioSeleccionadoIndex by remember { mutableStateOf(0) }
    val usuarioActual = listaUsuarios[usuarioSeleccionadoIndex]

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Selector de Perfiles de Ejemplo
        item {
            Text(
                text = "Seleccionar Perfil de Ejemplo",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                listaUsuarios.forEachIndexed { index, usuario ->
                    SegmentedButton(
                        selected = usuarioSeleccionadoIndex == index,
                        onClick = { usuarioSeleccionadoIndex = index },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = listaUsuarios.size)
                    ) {
                        Text(
                            text = usuario.nombre.split(" ")[0],
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }

        // Tarjeta Principal con Avatar y Nombre
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = usuarioActual.nombre,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    AssistChip(
                        onClick = { },
                        label = { Text(usuarioActual.rol) },
                        leadingIcon = {
                            Icon(
                                imageVector = usuarioActual.iconoRol,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
        }

        // Tarjeta con Información Detallada
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Ficha del Trabajador",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    HorizontalDivider()

                    PerfilInfoItem(
                        icon = Icons.Default.Business,
                        titulo = "Agencia / Operador",
                        valor = usuarioActual.agencia
                    )

                    PerfilInfoItem(
                        icon = Icons.Default.Badge,
                        titulo = "RUT / ID Identificación",
                        valor = usuarioActual.rut
                    )

                    PerfilInfoItem(
                        icon = Icons.Default.Phone,
                        titulo = "Contacto Directo",
                        valor = usuarioActual.telefono
                    )

                    PerfilInfoItem(
                        icon = usuarioActual.iconoRol,
                        titulo = usuarioActual.detalleExtraTitulo,
                        valor = usuarioActual.detalleExtraValor
                    )

                    PerfilInfoItem(
                        icon = Icons.Default.Star,
                        titulo = "Desempeño / Valoración",
                        valor = usuarioActual.calificacion
                    )
                }
            }
        }
    }
}

@Composable
fun PerfilInfoItem(
    icon: ImageVector,
    titulo: String,
    valor: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}