package aqilla.com.pitpitpetcare.api

import aqilla.com.pitpitpetcare.data.source.response.CustomerResponse
import aqilla.com.pitpitpetcare.data.source.response.LoginResponse
import aqilla.com.pitpitpetcare.data.source.response.RegisterResponse
import aqilla.com.pitpitpetcare.data.source.response.UserResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UserService {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") username: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<RegisterResponse>

    @FormUrlEncoded
    @PUT("user/pelanggan/{pelanggan_id}")
    suspend fun updateDataCustomer(
        @Header("Authorization") auth: String,
        @Path("pelanggan_id") customerId: Int,
        @Field("nama_pelanggan") customerName: String,
        @Field("alamat") address: String,
        @Field("telepon") phoneNumber: String,
    ): Response<CustomerResponse>

    @GET("user")
    suspend fun user(
        @Header("Authorization") auth: String,
    ): Response<UserResponse>


    @FormUrlEncoded
    @PUT("user/change-password")
    suspend fun changePassword(
        @Header("Authorization") auth: String,
        @Field("current_password") current_password: String,
        @Field("new_password") new_password: String,
        @Field("confirm_password") confirm_password: String,
    ): Response<CustomerResponse>

    @FormUrlEncoded
    @PUT("user/change-email")
    suspend fun changeEmail(
        @Header("Authorization") auth: String,
        @Field("new_email") new_email: String,
        @Field("password") password: String,
    ): Response<CustomerResponse>

    @Multipart
    @POST("user/change-photo-customer/{pelanggan_id}")
    suspend fun changePhotoProfileCustomer(
        @Header("Authorization") auth: String,
        @Path("pelanggan_id") pelanggan_id: Int,
        @Part foto: MultipartBody.Part
    ): Response<CustomerResponse>
}