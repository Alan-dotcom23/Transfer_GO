package com.proyecto.tgo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.proyecto.tgo.ui.components.ServicioCard
import com.proyecto.tgo.viewmodel.ServicioViewModel

@Composable
fun ServicioListScreen(viewModel: ServicioViewModel) {
    val servicios = viewModel.serviciosState.value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(servicios) { servicio ->
            ServicioCard(
                servicio = servicio,
                onConfirmarClick = { id -> viewModel.responderAsignacion(id) }
            )
        }
    }
}