package com.proyecto.tgo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.tgo.model.EstadoServicio
import com.proyecto.tgo.model.MenuSeccion
import com.proyecto.tgo.model.ServicioTuristico
import com.proyecto.tgo.repository.ServicioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

sealed interface ServiciosState {
    data object Loading : ServiciosState
    data object Empty : ServiciosState
    data class Success(val lista: List<ServicioTuristico>) : ServiciosState
    data class Error(val mensaje: String) : ServiciosState
}

class ServicioViewModel(
    private val repository: ServicioRepository = ServicioRepository()
) : ViewModel() {

    private val _seccionActual = MutableStateFlow(MenuSeccion.SERVICIOS)
    val seccionActual: StateFlow<MenuSeccion> = _seccionActual.asStateFlow()

    private val _serviciosState = MutableStateFlow<ServiciosState>(ServiciosState.Loading)
    val serviciosState: StateFlow<ServiciosState> = _serviciosState.asStateFlow()

    private val _diasBloqueados = MutableStateFlow<List<LocalDate>>(emptyList())
    val diasBloqueados: StateFlow<List<LocalDate>> = _diasBloqueados.asStateFlow()

    private val _mensajeUsuario = MutableStateFlow<String?>(null)
    val mensajeUsuario: StateFlow<String?> = _mensajeUsuario.asStateFlow()

    init {
        cargarServicios()
    }

    fun cambiarSeccion(seccion: MenuSeccion) {
        _seccionActual.value = seccion
    }

    fun cargarServicios() {
        viewModelScope.launch {
            _serviciosState.value = ServiciosState.Loading
            try {
                val lista = repository.obtenerServicios()
                _serviciosState.value = if (lista.isEmpty()) ServiciosState.Empty else ServiciosState.Success(lista)
            } catch (e: Exception) {
                _serviciosState.value = ServiciosState.Error("Error al obtener servicios: ${e.localizedMessage}")
            }
        }
    }

    fun actualizarEstadoServicio(id: String, estado: EstadoServicio) {
        viewModelScope.launch {
            repository.actualizarEstado(id, estado)
            cargarServicios()
        }
    }

    fun solicitarBloqueoFecha(fecha: LocalDate) {
        val hoy = LocalDate.now()
        val diasDiferencia = ChronoUnit.DAYS.between(hoy, fecha)

        if (fecha.isBefore(hoy)) {
            _mensajeUsuario.value = "No es posible bloquear fechas pasadas"
            return
        }

        if (diasDiferencia < 7) {
            _mensajeUsuario.value = "Las solicitudes deben realizarse con al menos 7 días de anticipación"
            return
        }

        _diasBloqueados.value = (_diasBloqueados.value + fecha).distinct()
        _mensajeUsuario.value = "Fecha $fecha bloqueada exitosamente"
    }

    fun limpiarMensaje() {
        _mensajeUsuario.value = null
    }
}