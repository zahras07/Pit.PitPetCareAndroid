package aqilla.com.pitpitpetcare.data.source.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import aqilla.com.pitpitpetcare.api.ApiConfig
import aqilla.com.pitpitpetcare.data.source.response.BookingResponse
import aqilla.com.pitpitpetcare.data.source.response.PostPutDelBookingResponse
import org.json.JSONObject

class BookingRepository(private val apiConfig: ApiConfig) {

    //create booking
    fun createBooking(
        auth: String,
        dokter_id: Int?,
        pelanggan_id: Int,
        hewan_id: Int,
        layanan_id: Int,
        paket_id: Int,
        booking_date: String,
        delivery_time: String,
        pickup_time: String,
        total: String
    ): LiveData<ResultProcess<PostPutDelBookingResponse>> = liveData {
        emit(ResultProcess.Loading)
        try {
            val response = apiConfig.getBookingApiService().booking(
                "Bearer $auth",
                dokter_id, pelanggan_id, hewan_id, layanan_id, paket_id, booking_date,delivery_time, pickup_time, total
            )
            if (response.isSuccessful){
                emit(ResultProcess.Success(response.body()!!))
            }else{
                // Tangani kode status selain 2xx
                when (response.code()) {
                    400 -> {
                        // Tangani kesalahan validasi
                        // Mengecek apakah respons kesalahan tidak null
                        try {
                            // Menganalisis string respons menjadi objek JSON
                            val jsonError = JSONObject(response.errorBody()!!.string())

                            // Mengambil nilai dari kunci "message" jika ada
                            val errorMessage = jsonError.optString("message")

                            // Sekarang, errorMessage berisi nilai pesan kesalahan jika ada
                            if (errorMessage.isNotEmpty()) {
                                emit(ResultProcess.Error(errorMessage))
                            } else {
                                emit(ResultProcess.Error("Permintaan tidak dapat diproses"))
                            }
                        } catch (e: Exception) {
                            emit(ResultProcess.Error("Permintaan tidak dapat diproses"))
                        }
                    }
                    401 -> {
                        // Tangani kode status 401 (Unauthorized)
                        emit(ResultProcess.Error(response.code().toString()))
                    }
                    // Tambahkan penanganan kode status lain jika diperlukan
                    else -> {

                        emit(ResultProcess.Error("Permintaan tidak dapat diproses"))
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("Booking Log : ", "OnFailure" + e.message.toString())
            emit(ResultProcess.Error(e.message.toString()))
        }
    }

    //show booking
    fun booking(
        auth: String,
    ): LiveData<ResultProcess<BookingResponse>> = liveData {
        emit(ResultProcess.Loading)
        try {
            val response = apiConfig.getBookingApiService().booking("Bearer $auth")
            if (response.isSuccessful){
                emit(ResultProcess.Success(response.body()!!))
            }else{
                // Tangani kode status selain 2xx
                when (response.code()) {
                    401 -> {
                        // Tangani kode status 401 (Unauthorized)
                        emit(ResultProcess.Error(response.code().toString()))
                    }
                    // Tambahkan penanganan kode status lain jika diperlukan
                    else -> {

                        emit(ResultProcess.Error("Permintaan tidak dapat diproses"))
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("Booking Log : ", "OnFailure" + e.message.toString())
            emit(ResultProcess.Error(e.message.toString()))
        }
    }

    //cancel booking
    fun cancelbooking(
        auth: String,
        transactionId: Int
    ): LiveData<ResultProcess<PostPutDelBookingResponse>> = liveData {
        emit(ResultProcess.Loading)
        try {
            val response = apiConfig.getBookingApiService().booking("Bearer $auth", transactionId)
            if (response.isSuccessful){
                emit(ResultProcess.Success(response.body()!!))
            }else{
                // Tangani kode status selain 2xx
                when (response.code()) {
                    400 -> {
                        // Tangani kesalahan validasi
                        // Mengecek apakah respons kesalahan tidak null
                        try {
                            // Menganalisis string respons menjadi objek JSON
                            val jsonError = JSONObject(response.errorBody()!!.string())

                            // Mengambil nilai dari kunci "message" jika ada
                            val errorMessage = jsonError.optString("message")

                            // Sekarang, errorMessage berisi nilai pesan kesalahan jika ada
                            if (errorMessage.isNotEmpty()) {
                                emit(ResultProcess.Error(errorMessage))
                            } else {
                                emit(ResultProcess.Error("Permintaan tidak dapat diproses"))
                            }
                        } catch (e: Exception) {
                            emit(ResultProcess.Error("Permintaan tidak dapat diproses"))
                        }
                    }
                    401 -> {
                        // Tangani kode status 401 (Unauthorized)
                        emit(ResultProcess.Error(response.code().toString()))
                    }
                    // Tambahkan penanganan kode status lain jika diperlukan
                    else -> {

                        emit(ResultProcess.Error("Permintaan tidak dapat diproses"))
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("Booking Log : ", "OnFailure" + e.message.toString())
            emit(ResultProcess.Error(e.message.toString()))
        }
    }
}