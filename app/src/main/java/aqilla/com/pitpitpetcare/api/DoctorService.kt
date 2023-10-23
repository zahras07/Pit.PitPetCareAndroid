package aqilla.com.pitpitpetcare.api

import aqilla.com.pitpitpetcare.data.source.response.DetailDoctorResponse
import aqilla.com.pitpitpetcare.data.source.response.DoctorResponse
import aqilla.com.pitpitpetcare.data.source.response.ScheduleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface DoctorService {


    @GET("dokter")
    suspend fun doctor(@Header("Authorization") auth: String): Response<DoctorResponse>

    @GET("dokter/jadwal")
    suspend fun schedule(@Header("Authorization") auth: String): Response<ScheduleResponse>

    @GET("dokter/detail/{id}")
    suspend fun doctorDetail(
        @Header("Authorization") auth: String,
        @Path("id") id: Int
    ): Response<DetailDoctorResponse>
}