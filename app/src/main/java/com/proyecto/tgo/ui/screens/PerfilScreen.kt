package com.proyecto.tgo.ui.screens

import android.content.ActivityNotFoundException
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecto.tgo.util.FotoPerfilStorage

@Composable
fun PerfilScreen(
    modifier: Modifier = Modifier
) {
    var isEditing by remember { mutableStateOf(false) }

    // Estados con datos de ejemplo del perfil
    var nombre by remember { mutableStateOf("Juan Pérez") }
    var rut by remember { mutableStateOf("12.345.678-9") }
    var telefono by remember { mutableStateOf("+56 9 1234 5678") }
    var email by remember { mutableStateOf("juan.perez@transfergo.cl") }
    var notificacionesActivas by remember { mutableStateOf(true) }

    // Datos del vehículo
    var isEditingVehiculo by remember { mutableStateOf(false) }
    var marcaModelo by remember { mutableStateOf("Toyota Hiace") }
    var patenteVehiculo by remember { mutableStateOf("KXYZ-89") }
    var colorVehiculo by remember { mutableStateOf("Blanco") }
    var capacidadPasajeros by remember { mutableIntStateOf(10) }

    // Recurso nativo: cámara para la foto de perfil
    val context = LocalContext.current
    var fotoPerfil by remember { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(Unit) { fotoPerfil = FotoPerfilStorage.cargar(context) }

    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            fotoPerfil = bitmap
            FotoPerfilStorage.guardar(context, bitmap)
        }
    }
    val abrirCamara = {
        try {
            camaraLauncher.launch(null)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No hay una aplicación de cámara disponible", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cabecera con Avatar e Información Principal
        ProfileHeader(
            nombre = nombre,
            rol = "Conductor / Transportista",
            foto = fotoPerfil,
            onCambiarFoto = abrirCamara
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Tarjeta de Datos Personales
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Información Personal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { isEditing = !isEditing }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                HorizontalDivider()

                if (isEditing) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre Completo") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = telefono,
                        onValueChange = { telefono = it },
                        label = { Text("Teléfono") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo Electrónico") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Button(
                        onClick = { isEditing = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Guardar Cambios")
                    }
                } else {
                    ProfileItem(icon = Icons.Default.Person, title = "RUT / ID", value = rut)
                    ProfileItem(icon = Icons.Default.Phone, title = "Teléfono", value = telefono)
                    ProfileItem(icon = Icons.Default.Email, title = "Correo", value = email)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjeta del Vehículo (incluye capacidad de pasajeros)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Mi Vehículo",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { isEditingVehiculo = !isEditingVehiculo }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar vehículo",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                HorizontalDivider()

                if (isEditingVehiculo) {
                    OutlinedTextField(
                        value = marcaModelo,
                        onValueChange = { marcaModelo = it },
                        label = { Text("Marca y modelo") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = patenteVehiculo,
                        onValueChange = { patenteVehiculo = it.uppercase() },
                        label = { Text("Patente") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = colorVehiculo,
                        onValueChange = { colorVehiculo = it },
                        label = { Text("Color") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // Selector de capacidad de pasajeros (1 a 60)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Capacidad de pasajeros",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            FilledTonalIconButton(
                                onClick = { if (capacidadPasajeros > 1) capacidadPasajeros-- }
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Disminuir capacidad")
                            }
                            Text(
                                text = capacidadPasajeros.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            FilledTonalIconButton(
                                onClick = { if (capacidadPasajeros < 60) capacidadPasajeros++ }
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Aumentar capacidad")
                            }
                        }
                    }

                    Button(
                        onClick = { isEditingVehiculo = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Guardar Vehículo")
                    }
                } else {
                    ProfileItem(icon = Icons.Default.DirectionsCar, title = "Marca y modelo", value = marcaModelo)
                    ProfileItem(icon = Icons.Default.Pin, title = "Patente", value = patenteVehiculo)
                    ProfileItem(icon = Icons.Default.ColorLens, title = "Color", value = colorVehiculo)
                    ProfileItem(
                        icon = Icons.Default.Groups,
                        title = "Capacidad",
                        value = if (capacidadPasajeros == 1) "1 pasajero" else "$capacidadPasajeros pasajeros"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjeta de Ajustes y Preferencias
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Ajustes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                HorizontalDivider()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text(text = "Notificaciones de servicios", style = MaterialTheme.typography.bodyMedium)
                    }
                    Switch(
                        checked = notificacionesActivas,
                        onCheckedChange = { notificacionesActivas = it }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text(text = "Cambiar Contraseña", style = MaterialTheme.typography.bodyMedium)
                    }
                    TextButton(onClick = { /* Lógica de contraseña */ }) {
                        Text("Editar")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de Cerrar Sesión
        OutlinedButton(
            onClick = { /* Lógica para cerrar sesión */ },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
            border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.error)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cerrar Sesión")
        }
    }
}

@Composable
private fun ProfileHeader(
    nombre: String,
    rol: String,
    foto: Bitmap?,
    onCambiarFoto: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(96.dp)) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                if (foto != null) {
                    Image(
                        bitmap = foto.asImageBitmap(),
                        contentDescription = "Foto de Perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Foto de Perfil",
                        modifier = Modifier.size(56.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Botón de cámara sobre el avatar
            FilledIconButton(
                onClick = onCambiarFoto,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Tomar foto de perfil",
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = nombre,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = rol,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
private fun ProfileItem(
    icon: ImageVector,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}