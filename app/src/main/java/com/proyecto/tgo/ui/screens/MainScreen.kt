package com.proyecto.tgo.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyecto.tgo.model.MenuSeccion
import com.proyecto.tgo.ui.components.AppDrawer
import com.proyecto.tgo.viewmodel.ServicioViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: ServicioViewModel = viewModel()
) {
    val seccionActual by viewModel.seccionActual.collectAsStateWithLifecycle()
    val mensajeUsuario by viewModel.mensajeUsuario.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(mensajeUsuario) {
        mensajeUsuario?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.limpiarMensaje()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        // En el mapa se desactiva el gesto lateral para no chocar con el desplazamiento del mapa
        gesturesEnabled = seccionActual != MenuSeccion.MAPA,
        drawerContent = {
            AppDrawer(
                seccionSeleccionada = seccionActual,
                onSeccionSeleccionada = { nuevaSeccion ->
                    viewModel.cambiarSeccion(nuevaSeccion)
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(seccionActual.titulo) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (seccionActual) {
                    MenuSeccion.SERVICIOS -> ServicioListScreen()
                    MenuSeccion.MAPA -> MapaScreen()
                    MenuSeccion.DISPONIBILIDAD -> DisponibilidadScreen()
                    MenuSeccion.PERFIL -> PerfilScreen()
                }
            }
        }
    }
}