package com.proyecto.tgo.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecto.tgo.viewmodel.ServicioViewModel
import com.proyecto.tgo.ui.components.ClimaCard
import com.proyecto.tgo.ui.components.IniciarViajeDialog
import com.proyecto.tgo.util.ViajeScheduler
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ServicioItem(
    val id: String,
    val pasajero: String,
    val telefono: String,
    val origen: String,
    val destino: String,
    val hora: String,
    val tarifa: String,
    val estado: EstadoServicio,
    val agendadoPara: LocalDateTime? = null
)

enum class EstadoServicio(val titulo: String) {
    PENDIENTE("Pendiente"),
    EN_CURSO("En Curso"),
    COMPLETADO("Completado")
}

@Composable
fun ServicioListScreen(
    modifier: Modifier = Modifier,
    viewModel: ServicioViewModel? = null
) {
    var queryBusqueda by remember { mutableStateOf("") }
    var estadoFiltroSeleccionado by remember { mutableStateOf<EstadoServicio?>(null) }

    val context = LocalContext.current
    var servicioAIniciar by remember { mutableStateOf<ServicioItem?>(null) }

    // Permiso de notificaciones (Android 13+) para avisar la hora agendada
    val permisoNotificaciones = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (!concedido) {
            Toast.makeText(
                context,
                "Sin permiso de notificaciones no recibirás el recordatorio del viaje",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    val serviciosEjemplo = remember {
        mutableStateListOf(
            ServicioItem("TR-101", "María González", "+56 9 8765 4321", "Aeropuerto El Tepual", "Hotel Cabaña del Lago", "10:30 AM", "$25.000", EstadoServicio.PENDIENTE),
            ServicioItem("TR-102", "Carlos Silva", "+56 9 1122 3344", "Terminal de Buses", "Centro de Puerto Montt", "11:15 AM", "$12.000", EstadoServicio.EN_CURSO),
            ServicioItem("TR-103", "Empresa X", "+56 9 9988 7766", "Plaza de Armas", "Puerto Varas Centro", "08:00 AM", "$30.000", EstadoServicio.COMPLETADO)
        )
    }

    val serviciosFiltrados = serviciosEjemplo.filter { servicio ->
        val cumpleFiltroEstado = estadoFiltroSeleccionado == null || servicio.estado == estadoFiltroSeleccionado
        val cumpleBusqueda = servicio.pasajero.contains(queryBusqueda, ignoreCase = true) ||
                servicio.origen.contains(queryBusqueda, ignoreCase = true) ||
                servicio.destino.contains(queryBusqueda, ignoreCase = true) ||
                servicio.id.contains(queryBusqueda, ignoreCase = true)

        cumpleFiltroEstado && cumpleBusqueda
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        ClimaCard()
        // 1. Buscador rápido
        OutlinedTextField(
            value = queryBusqueda,
            onValueChange = { queryBusqueda = it },
            placeholder = { Text("Buscar por pasajero, origen o ID...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 2. Filtros horizontales desplazables (Evita el texto apretado)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                FilterChip(
                    selected = estadoFiltroSeleccionado == null,
                    onClick = { estadoFiltroSeleccionado = null },
                    label = { Text("Todos") }
                )
            }
            item {
                FilterChip(
                    selected = estadoFiltroSeleccionado == EstadoServicio.PENDIENTE,
                    onClick = { estadoFiltroSeleccionado = EstadoServicio.PENDIENTE },
                    label = { Text("Pendientes") }
                )
            }
            item {
                FilterChip(
                    selected = estadoFiltroSeleccionado == EstadoServicio.EN_CURSO,
                    onClick = { estadoFiltroSeleccionado = EstadoServicio.EN_CURSO },
                    label = { Text("En Curso") }
                )
            }
            item {
                FilterChip(
                    selected = estadoFiltroSeleccionado == EstadoServicio.COMPLETADO,
                    onClick = { estadoFiltroSeleccionado = EstadoServicio.COMPLETADO },
                    label = { Text("Completados") }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3. Lista de Servicios
        if (serviciosFiltrados.isEmpty()) {
            EmptyStateServicios()
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(serviciosFiltrados, key = { it.id }) { servicio ->
                    ServicioCard(
                        servicio = servicio,
                        onLlamar = { marcarTelefono(context, servicio.telefono) },
                        onIniciar = { servicioAIniciar = servicio },
                        onNavegar = { abrirNavegacion(context, servicio.destino) },
                        onQuitarAgenda = {
                            ViajeScheduler.cancelar(context, servicio.id)
                            val i = serviciosEjemplo.indexOfFirst { it.id == servicio.id }
                            if (i >= 0) serviciosEjemplo[i] = serviciosEjemplo[i].copy(agendadoPara = null)
                        }
                    )
                }
            }
        }
    }

    // Diálogo: iniciar ahora o agendar horario
    servicioAIniciar?.let { servicio ->
        IniciarViajeDialog(
            servicioId = servicio.id,
            onCerrar = { servicioAIniciar = null },
            onIniciarAhora = {
                ViajeScheduler.cancelar(context, servicio.id)
                val i = serviciosEjemplo.indexOfFirst { it.id == servicio.id }
                if (i >= 0) {
                    serviciosEjemplo[i] = serviciosEjemplo[i].copy(
                        estado = EstadoServicio.EN_CURSO,
                        agendadoPara = null
                    )
                }
                servicioAIniciar = null
            },
            onAgendar = { momento ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    permisoNotificaciones.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                val descripcion = "${servicio.pasajero} · ${servicio.origen} → ${servicio.destino}"
                if (ViajeScheduler.agendar(context, servicio.id, descripcion, momento)) {
                    val i = serviciosEjemplo.indexOfFirst { it.id == servicio.id }
                    if (i >= 0) serviciosEjemplo[i] = serviciosEjemplo[i].copy(agendadoPara = momento)
                    Toast.makeText(
                        context,
                        "Viaje agendado para ${momento.format(FORMATO_AGENDA)}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(context, "El horario ya pasó", Toast.LENGTH_SHORT).show()
                }
                servicioAIniciar = null
            }
        )
    }
}

private val FORMATO_AGENDA: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

// ---- Recursos nativos: teléfono y navegación ----

/** Abre el marcador del teléfono con el número (no requiere permisos). */
private fun marcarTelefono(context: android.content.Context, telefono: String) {
    val numero = telefono.filter { it.isDigit() || it == '+' }
    try {
        context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$numero")))
    } catch (e: Exception) {
        Toast.makeText(context, "No se pudo abrir el marcador", Toast.LENGTH_SHORT).show()
    }
}

/** Abre la app de mapas del teléfono con la ruta hacia el destino. */
private fun abrirNavegacion(context: android.content.Context, destino: String) {
    val uri = Uri.parse("geo:0,0?q=${Uri.encode(destino)}")
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    } catch (e: Exception) {
        Toast.makeText(context, "No hay una app de mapas instalada", Toast.LENGTH_SHORT).show()
    }
}

@Composable
private fun ServicioCard(
    servicio: ServicioItem,
    onLlamar: () -> Unit,
    onIniciar: () -> Unit,
    onNavegar: () -> Unit,
    onQuitarAgenda: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Cabecera: ID, Hora y Estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = servicio.id,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "•  ${servicio.hora}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                EstadoBadge(estado = servicio.estado)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

            // Ruta origen -> destino con mejor espaciado
            RutaVisual(origen = servicio.origen, destino = servicio.destino)

            // Horario agendado para iniciar el viaje
            servicio.agendadoPara?.let { momento ->
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, top = 2.dp, bottom = 2.dp, end = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = "Agendado: ${momento.format(FORMATO_AGENDA)}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                        )
                        IconButton(onClick = onQuitarAgenda) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Quitar horario agendado",
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

            // Pie de tarjeta: Datos del Pasajero + Acciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = servicio.pasajero,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = "Tarifa: ${servicio.tarifa}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Botones de acción bien dimensionados
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón de llamada más amplio y cómodo
                    FilledTonalIconButton(
                        onClick = onLlamar,
                        modifier = Modifier.size(44.dp),
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Llamar",
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    when (servicio.estado) {
                        EstadoServicio.PENDIENTE -> {
                            Button(
                                onClick = onIniciar,
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    if (servicio.agendadoPara == null) "Iniciar" else "Reagendar",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        EstadoServicio.EN_CURSO -> {
                            Button(
                                onClick = onNavegar,
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Navegar", fontWeight = FontWeight.SemiBold)
                            }
                        }
                        EstadoServicio.COMPLETADO -> {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RutaVisual(origen: String, destino: String) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = origen,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = destino,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
private fun EstadoBadge(estado: EstadoServicio) {
    val (bgColor, textColor) = when (estado) {
        EstadoServicio.PENDIENTE -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        EstadoServicio.EN_CURSO -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        EstadoServicio.COMPLETADO -> MaterialTheme.colorScheme.surfaceContainerHigh to MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            text = estado.titulo,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
private fun EmptyStateServicios() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.DirectionsCar,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "No hay servicios disponibles",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = "Intenta cambiar el filtro o el término de búsqueda.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}