package aqilla.com.pitpitpetcare.api

import aqilla.com.pitpitpetcare.data.source.response.AnimalResponse
import aqilla.com.pitpitpetcare.data.source.response.JenisHewanResponse
import aqilla.com.pitpitpetcare.data.source.response.PostPutDelAnimalResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AnimalService {

    @GET("hewan/{pelanggan_id}")
    suspend fun animal(
        @Header("Authorization") auth: String,
        @Path("pelanggan_id") customerId: Int,
    ): Response<AnimalResponse>

    @FormUrlEncoded
    @POST("hewan")
    suspend fun createAnimal(
        @Header("Authorization") auth: String,
        @Field("nama_hewan") nama_hewan: String,
        @Field("jenis_hewan") jenis_hewan: Int,
        @Field("umur") umur: String,
        @Field("berat") berat: String,
    ): Response<PostPutDelAnimalResponse>


    @GET("jenis-hewan")
    suspend fun jenisHewan(
    ): Response<JenisHewanResponse>
}