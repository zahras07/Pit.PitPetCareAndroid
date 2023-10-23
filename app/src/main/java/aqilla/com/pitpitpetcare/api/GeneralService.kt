package aqilla.com.pitpitpetcare.api

import aqilla.com.pitpitpetcare.data.source.response.LayananResponse
import aqilla.com.pitpitpetcare.data.source.response.PaketResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GeneralService {

    @GET("paket/{layanan_id}")
    suspend fun paket(
        @Path("layanan_id") layananId: Int,
    ): Response<PaketResponse>

    @GET("layanan")
    suspend fun layanan(): Response<LayananResponse>

    @GET("paket")
    suspend fun allPaket(): Response<PaketResponse>
}