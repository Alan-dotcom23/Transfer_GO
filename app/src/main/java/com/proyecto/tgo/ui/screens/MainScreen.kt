package com.proyecto.tgo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.proyecto.tgo.model.MenuSeccion
import com.proyecto.tgo.ui.components.AppDrawerContent
import com.proyecto.tgo.viewmodel.ServicioViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: ServicioViewModel = remember { ServicioViewModel() }) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val seccionActual by viewModel.seccionActual
    val mensajeSnackbar by viewModel.mensajeSnackbar

    LaunchedEffect(mensajeSnackbar) {
        mensajeSnackbar?.let { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
            viewModel.limpiarMensajeSnackbar()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                seccionActual = seccionActual,
                onSeccionSeleccionada = { seccion ->
                    viewModel.seleccionarSeccion(seccion)
                },
                onCloseDrawer = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = { Text(seccionActual.titulo) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Abrir Menú",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (seccionActual) {
                    MenuSeccion.SERVICIOS -> ServicioListScreen(viewModel = viewModel)
                    MenuSeccion.DISPONIBILIDAD -> DisponibilidadScreen(
                        onSolicitarBloqueo = { fecha ->
                            viewModel.solicitarBloqueoFecha(fecha)
                        }
                    )
                    MenuSeccion.PERFIL -> PerfilScreen()
                }
            }
        }
    }
}