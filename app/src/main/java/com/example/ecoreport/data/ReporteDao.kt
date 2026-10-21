package com.example.ecoreport.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ReporteDao {

    @Insert
    suspend fun insertar(
        reporte: ReporteEntity
    )

    @Query(
        "SELECT * FROM reportes ORDER BY id DESC"
    )
    fun obtenerTodos(): Flow<List<ReporteEntity>>

    @Query(
        """
            SELECT * 
            FROM reportes 
            WHERE (:categoria IS NULL OR :categoria = '' OR categoria LIKE '%' || :categoria || '%')
            ORDER BY id DESC
        """
    )
    fun obtenerPorCategoria(
        categoria: String?
    ): Flow<List<ReporteEntity>>

    @Query(
        """
            SELECT * 
            FROM reportes 
            WHERE (:estado IS NULL OR :estado = '' OR :estado = 'TODOS' OR UPPER(estado) = UPPER(:estado))
            ORDER BY id DESC
        """
    )
    fun obtenerPorEstado(
        estado: String?
    ): Flow<List<ReporteEntity>>

    @Query(
        """
            SELECT * 
            FROM reportes 
            WHERE (:categoria IS NULL OR :categoria = '' OR categoria LIKE '%' || :categoria || '%')
              AND (:estado IS NULL OR :estado = '' OR :estado = 'TODOS' OR UPPER(estado) = UPPER(:estado))
            ORDER BY id DESC
        """
    )
    fun obtenerFiltrados(
        categoria: String?,
        estado: String?
    ): Flow<List<ReporteEntity>>

    @Update
    suspend fun actualizar(
        reporte: ReporteEntity
    )

    @Delete
    suspend fun eliminar(
        reporte: ReporteEntity
    )
}
