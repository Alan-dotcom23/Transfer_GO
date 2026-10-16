package com.proyecto.tgo.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.proyecto.tgo.model.MenuSeccion
import com.proyecto.tgo.model.ServicioTuristico
import com.proyecto.tgo.repository.ServicioRepository

class ServicioViewModel : ViewModel() {

    private val repository = ServicioRepository()

    private val _serviciosState = mutableStateOf<List<ServicioTuristico>>(emptyList())
    val serviciosState: State<List<ServicioTuristico>> = _serviciosState

    // Estado observable de la sección activa
    private val _seccionActual = mutableStateOf(MenuSeccion.SERVICIOS)
    val seccionActual: State<MenuSeccion> = _seccionActual

    private val _mensajeSnackbar = mutableStateOf<String?>(null)
    val mensajeSnackbar: State<String?> = _mensajeSnackbar

    init {
        cargarServicios()
    }

    fun cargarServicios() {
        _serviciosState.value = repository.obtenerServicios()
    }

    fun seleccionarSeccion(seccion: MenuSeccion) {
        _seccionActual.value = seccion
        _mensajeSnackbar.value = "Navegando a: ${seccion.titulo}"
    }

    fun responderAsignacion(idServicio: String) {
        if (repository.confirmarServicio(idServicio)) {
            cargarServicios()
            _mensajeSnackbar.value = "Servicio $idServicio confirmado con éxito"
        }
    }

    fun limpiarMensajeSnackbar() {
        _mensajeSnackbar.value = null
    }

    fun solicitarBloqueoFecha(fecha: String) {
        _mensajeSnackbar.value = "Solicitud de bloqueo enviada para la fecha: $fecha (sujeta a validación de 7 días)."
    }
}