package com.example.ecoreport.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoreport.data.ReporteEntity
import com.example.ecoreport.data.remote.ReporteDto
import com.example.ecoreport.repository.ReporteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ReporteViewModel (
    private val repository: ReporteRepository
): ViewModel() {

    private val _categoriaSeleccionada = MutableStateFlow<String?>(null)
    val categoriaSeleccionada: StateFlow<String?> = _categoriaSeleccionada.asStateFlow()

    private val _estadoSeleccionado = MutableStateFlow<String?>(null)
    val estadoSeleccionado: StateFlow<String?> = _estadoSeleccionado.asStateFlow()

    private val _reportesRemotos = MutableStateFlow<List<ReporteDto>>(emptyList())
    val reportesRemotos: StateFlow<List<ReporteDto>> = _reportesRemotos.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val reportes: StateFlow<List<ReporteEntity>> =
        combine(_categoriaSeleccionada, _estadoSeleccionado) { categoria, estado ->
            Pair(categoria, estado)
        }
        .flatMapLatest { (categoria, estado) ->
            repository.obtenerFiltrados(categoria, estado)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun seleccionarCategoria(categoria: String?) {
        _categoriaSeleccionada.value = if (categoria.isNullOrBlank()) null else categoria.trim()
    }

    fun seleccionarEstado(estado: String?) {
        _estadoSeleccionado.value = if (estado.isNullOrBlank() || estado.equals("TODOS", ignoreCase = true)) null else estado.trim()
    }

    fun aplicarFiltros(categoria: String?, estado: String?) {
        seleccionarCategoria(categoria)
        seleccionarEstado(estado)
    }

    fun limpiarFiltros() {
        _categoriaSeleccionada.value = null
        _estadoSeleccionado.value = null
    }

    fun filtrarPorCategoria(categoria: String) {
        seleccionarCategoria(categoria)
    }

    fun filtrarPorEstado(estado: String) {
        seleccionarEstado(estado)
    }

    fun cargarReportes() {
        limpiarFiltros()
    }

    fun cargarReportesRemotos() {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            try {
                val remotos = repository.obtenerReportesRemotos()
                _reportesRemotos.value = remotos
                println("API_REMOTA: Reportes remotos obtenidos: ${remotos.size}")
                remotos.forEach {
                    println("API_REMOTA: ID=${it.id}, Titulo=${it.titulo}, Categoria=${it.categoria}, Estado=${it.estado}")
                }
            } catch (e: Exception) {
                _error.value = e.message
                println("API_REMOTA ERROR: ${e.message}")
            } finally {
                _cargando.value = false
            }
        }
    }

    fun insertar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.insertar(reporte)
        }
    }

    fun eliminar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.eliminar(reporte)
        }
    }

    fun actualizar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.actualizar(reporte)
        }
    }
}
