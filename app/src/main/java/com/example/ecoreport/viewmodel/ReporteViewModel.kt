package com.example.ecoreport.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ecoreport.data.ReporteEntity
import com.example.ecoreport.repository.ReporteRepository
import kotlinx.coroutines.launch

import androidx.compose.runtime.mutableStateListOf

class ReporteViewModel (
    private val repository: ReporteRepository
): ViewModel() {
    val reportes = mutableStateListOf<ReporteEntity>()

    init {
        cargarReportes()
    }

    fun cargarReportes() {
        viewModelScope.launch {
            val datos = repository.obtenerTodos()

            reportes.clear()
            reportes.addAll(datos)
        }
    }

    fun filtrarPorCategoria(categoria: String) {
        viewModelScope.launch {
            val datos = repository.obtenerPorCategoria(categoria)
            reportes.clear()
            reportes.addAll(datos)
        }
    }

    fun filtrarPorEstado(estado: String) {
        viewModelScope.launch {
            val datos = repository.obtenerPorEstado(estado)
            reportes.clear()
            reportes.addAll(datos)
        }
    }

    fun insertar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.insertar(reporte)
            cargarReportes()
        }
    }

    fun eliminar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.eliminar(reporte)

            cargarReportes()
        }
    }

    fun actualizar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.actualizar(reporte)

            cargarReportes()
        }
    }

    class Factory(private val repository: ReporteRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReporteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ReporteViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}