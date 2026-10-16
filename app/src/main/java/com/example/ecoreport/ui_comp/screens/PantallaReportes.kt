package com.example.ecoreport.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ecoreport.data.ReporteEntity

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun PantallaReportes(
    reportes: List<ReporteEntity>,
    seleccionarReporte: (ReporteEntity) -> Unit,
    filtrarPorCategoria: (String) -> Unit,
    filtrarPorEstado: (String) -> Unit,
    cargarTodos: () -> Unit,
    volver: () -> Unit
) {

    var expandedCategoria by remember { mutableStateOf(false) }
    var expandedEstado by remember { mutableStateOf(false) }
    var filtroActivo by remember { mutableStateOf("Todos") }

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

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Filtro por Estado
            Column {
                OutlinedButton(onClick = { expandedEstado = true }) {
                    Text("Filtrar Estado")
                }
                DropdownMenu(
                    expanded = expandedEstado,
                    onDismissRequest = { expandedEstado = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Pendiente") },
                        onClick = {
                            filtrarPorEstado("PENDIENTE")
                            filtroActivo = "Estado: Pendiente"
                            expandedEstado = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("En proceso") },
                        onClick = {
                            filtrarPorEstado("EN_PROCESO")
                            filtroActivo = "Estado: En proceso"
                            expandedEstado = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Resuelto") },
                        onClick = {
                            filtrarPorEstado("RESUELTO")
                            filtroActivo = "Estado: Resuelto"
                            expandedEstado = false
                        }
                    )
                }
            }

            // Filtro por Categoría
            Column {
                OutlinedButton(onClick = { expandedCategoria = true }) {
                    Text("Filtrar Categoría")
                }
                DropdownMenu(
                    expanded = expandedCategoria,
                    onDismissRequest = { expandedCategoria = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Contaminación") },
                        onClick = {
                            filtrarPorCategoria("Contaminación")
                            filtroActivo = "Categoría: Contaminación"
                            expandedCategoria = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Basura") },
                        onClick = {
                            filtrarPorCategoria("Basura")
                            filtroActivo = "Categoría: Basura"
                            expandedCategoria = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Deforestación") },
                        onClick = {
                            filtrarPorCategoria("Deforestación")
                            filtroActivo = "Categoría: Deforestación"
                            expandedCategoria = false
                        }
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        OutlinedButton(
            onClick = {
                filtroActivo = "Todos"
                cargarTodos()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Limpiar Filtros")
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Filtro actual: $filtroActivo",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        if (reportes.isEmpty()) {

            Text(
                text = if (filtroActivo == "Todos") "No existen reportes registrados." else "No existen reportes para el filtro seleccionado."
            )

        } else {

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(
                    items = reportes,
                    key = { reporte ->
                        reporte.id
                    }
                ) { reporte ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                seleccionarReporte(reporte)
                            },

                        shape = RoundedCornerShape(16.dp),

                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
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

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(
                                text = "Categoría: ${reporte.categoria}"
                            )

                            Text(
                                text = "Ubicación: ${reporte.ubicacion}"
                            )

                            Text(
                                text = "Estado: ${reporte.estado}"
                            )

                            Text(
                                text = "Fecha: ${reporte.fecha}"
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(
                                text = "Presiona para ver el detalle",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedButton(
            onClick = volver,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "VOLVER"
            )
        }
    }
}