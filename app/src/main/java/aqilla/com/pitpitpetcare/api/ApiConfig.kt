package aqilla.com.pitpitpetcare.api

import aqilla.com.pitpitpetcare.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

    private val retrofit =
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(
            client
        ).build()

    fun getUserService(): UserService {
        return retrofit.create(UserService::class.java)
    }

    fun getDoctorService(): DoctorService {
        return retrofit.create(DoctorService::class.java)
    }

    fun getAnimalService(): AnimalService {
        return retrofit.create(AnimalService::class.java)
    }

    fun getGeneralService(): GeneralService {
        return retrofit.create(GeneralService::class.java)
    }


    fun getBookingApiService() : BookingService{
        return retrofit.create(BookingService::class.java)
    }
}