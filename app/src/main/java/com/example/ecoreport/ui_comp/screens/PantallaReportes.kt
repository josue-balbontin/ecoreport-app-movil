package com.example.ecoreport.ui_comp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ecoreport.data.ReporteEntity

@Composable
fun PantallaReportes(
    reportes: List<ReporteEntity>,
    categoriaSeleccionada: String? = null,
    estadoSeleccionado: String? = null,
    seleccionarCategoria: (String?) -> Unit = {},
    seleccionarReporte: (ReporteEntity) -> Unit,
    onAplicarFiltros: (categoria: String?, estado: String?) -> Unit,
    onLimpiarFiltros: () -> Unit,
    volver: () -> Unit
) {
    var textoCategoria by remember(categoriaSeleccionada) {
        mutableStateOf(categoriaSeleccionada ?: "")
    }
    var estadoElegido by remember(estadoSeleccionado) {
        mutableStateOf(estadoSeleccionado)
    }
    var expandedEstado by remember { mutableStateOf(false) }

    val estados = listOf("TODOS", "PENDIENTE", "EN_PROCESO", "RESUELTO")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Reportes",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjeta con los controles de filtros
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Text(
                    text = "Filtros de búsqueda",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Estado (select)
                Column {
                    Text(
                        text = "Estado (select)",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedButton(
                        onClick = { expandedEstado = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = when (estadoElegido) {
                                null, "", "TODOS" -> "Todos los estados"
                                "PENDIENTE" -> "Pendiente"
                                "EN_PROCESO" -> "En proceso"
                                "RESUELTO" -> "Resuelto"
                                else -> estadoElegido!!
                            }
                        )
                    }
                    DropdownMenu(
                        expanded = expandedEstado,
                        onDismissRequest = { expandedEstado = false }
                    ) {
                        estados.forEach { estado ->
                            val etiqueta = when (estado) {
                                "TODOS" -> "Todos los estados"
                                "PENDIENTE" -> "Pendiente"
                                "EN_PROCESO" -> "En proceso"
                                "RESUELTO" -> "Resuelto"
                                else -> estado
                            }
                            DropdownMenuItem(
                                text = { Text(etiqueta) },
                                onClick = {
                                    estadoElegido = if (estado == "TODOS") null else estado
                                    expandedEstado = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Categoría (input)
                OutlinedTextField(
                    value = textoCategoria,
                    onValueChange = { textoCategoria = it },
                    label = { Text("Categoría (input)") },
                    placeholder = { Text("Ej: Contaminación, Basura...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Botones Buscar y Limpiar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val cat = textoCategoria.trim().ifEmpty { null }
                            seleccionarCategoria(cat)
                            onAplicarFiltros(cat, estadoElegido)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("BUSCAR")
                    }

                    OutlinedButton(
                        onClick = {
                            textoCategoria = ""
                            estadoElegido = null
                            seleccionarCategoria(null)
                            onLimpiarFiltros()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("LIMPIAR")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Indicador de filtros aplicados
        val hayFiltro = !categoriaSeleccionada.isNullOrBlank() || !estadoSeleccionado.isNullOrBlank()
        if (hayFiltro) {
            val detalleEstado = when (estadoSeleccionado) {
                null, "", "TODOS" -> null
                "PENDIENTE" -> "Pendiente"
                "EN_PROCESO" -> "En proceso"
                "RESUELTO" -> "Resuelto"
                else -> estadoSeleccionado
            }
            val filtroTexto = listOfNotNull(
                detalleEstado?.let { "Estado: $it" },
                categoriaSeleccionada?.let { "Categoría: $it" }
            ).joinToString(" | ")

            Text(
                text = "Filtro activo: $filtroTexto",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (reportes.isEmpty()) {
            Text(
                text = if (hayFiltro) "No existen reportes con los filtros seleccionados." else "No existen reportes registrados.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = reportes,
                    key = { reporte -> reporte.id }
                ) { reporte ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                seleccionarReporte(reporte)
                            },
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = reporte.titulo,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(text = "Categoría: ${reporte.categoria}")
                            Text(text = "Ubicación: ${reporte.ubicacion}")
                            Text(text = "Estado: ${reporte.estado}")
                            Text(text = "Fecha: ${reporte.fecha}")

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Presiona para ver el detalle",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = volver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "VOLVER")
        }
    }
}
