package com.example.ecoreport.data.remote

import retrofit2.http.GET

interface ReporteApiService {

    @GET("reportes")//BASE_URL = http://api.ejemplo.com/reportes
    suspend fun obtenerReportes():
            List<ReporteDto>
}
