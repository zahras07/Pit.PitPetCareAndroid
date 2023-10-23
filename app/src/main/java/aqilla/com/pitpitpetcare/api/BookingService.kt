package aqilla.com.pitpitpetcare.api

import aqilla.com.pitpitpetcare.data.source.response.BookingResponse
import aqilla.com.pitpitpetcare.data.source.response.PostPutDelBookingResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface BookingService {

    @FormUrlEncoded
    @POST("booking")
    suspend fun booking(
        @Header("Authorization") auth: String,
        @Field("dokter_id") dokter_id: Int?,
        @Field("pelanggan_id") pelanggan_id: Int,
        @Field("hewan_id") hewan_id: Int,
        @Field("layanan_id") layanan_id: Int,
        @Field("paket_id") paket_id: Int,
        @Field("booking_date") booking_date: String,
        @Field("delivery_time") deliveryTime: String,
        @Field("pickup_time") pickUpTime: String,
        @Field("total") total: String
    ): Response<PostPutDelBookingResponse>

    @GET("booking")
    suspend fun booking(@Header("Authorization") auth: String): Response<BookingResponse>

    @PUT("booking/cancel/{transaction_id}")
    suspend fun booking(
        @Header("Authorization") auth: String,
        @Path("transaction_id") transaction_id: Int
    ): Response<PostPutDelBookingResponse>
}