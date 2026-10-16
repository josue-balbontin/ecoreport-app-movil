package com.example.ecoreport.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ReporteDao {

    @Insert
    suspend fun insertar(
        reporte: ReporteEntity
    )

    @Query(
        "SELECT * FROM reportes ORDER BY id DESC"
    )
    suspend fun obtenerTodos():
            List<ReporteEntity>

    /*
    SE NECESITA AGREGAR UN NUEVO QUERY QUE NOS PERMITA REALIZAR CONSULTAS
    MEDIANTE FILTROS UTILIZANDO EL WHERE
    */
    @Query(
        "SELECT * FROM reportes WHERE categoria = :categoria COLLATE NOCASE ORDER BY id DESC"
    )
    suspend fun obtenerPorCategoria(categoria: String): List<ReporteEntity>

    @Query(
        "SELECT * FROM reportes WHERE estado = :estado COLLATE NOCASE ORDER BY id DESC"
    )
    suspend fun obtenerPorEstado(estado: String): List<ReporteEntity>




    @Update
    suspend fun actualizar(
        reporte: ReporteEntity
    )

    @Delete
    suspend fun eliminar(
        reporte: ReporteEntity
    )
}