package aqilla.com.pitpitpetcare.data.source.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import aqilla.com.pitpitpetcare.api.ApiConfig
import aqilla.com.pitpitpetcare.data.source.response.DetailDoctorResponse
import aqilla.com.pitpitpetcare.data.source.response.DoctorResponse
import aqilla.com.pitpitpetcare.data.source.response.ScheduleResponse
import org.json.JSONObject

class DoctorRepository(private val apiConfig: ApiConfig) {

    //fungsi untuk mengambil data dokter
    fun doctor(auth: String): LiveData<ResultProcess<DoctorResponse>> = liveData {
        emit(ResultProcess.Loading)

        try {
            val response = apiConfig.getDoctorService().doctor("Bearer $auth")
            if (response.isSuccessful) {
                emit(ResultProcess.Success(response.body()!!))
            } else {
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
            Log.e("Doctor Log : ", "OnFailure" + e.message.toString())
            emit(ResultProcess.Error(e.message.toString()))
        }
    }

    fun schedule(auth: String): LiveData<ResultProcess<ScheduleResponse>> = liveData {
        emit(ResultProcess.Loading)

        try {
            val response = apiConfig.getDoctorService().schedule("Bearer $auth")
            if (response.isSuccessful) {
                emit(ResultProcess.Success(response.body()!!))
            } else {
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
            Log.e("Doctor Log : ", "OnFailure" + e.message.toString())
            emit(ResultProcess.Error(e.message.toString()))
        }
    }

    fun doctorDetail(auth: String, id: Int): LiveData<ResultProcess<DetailDoctorResponse>> =
        liveData {
            emit(ResultProcess.Loading)

            try {
                val response = apiConfig.getDoctorService().doctorDetail("Bearer $auth", id)
                if (response.isSuccessful) {
                    emit(ResultProcess.Success(response.body()!!))
                } else {
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
                Log.e("Doctor Log : ", "OnFailure" + e.message.toString())
                emit(ResultProcess.Error(e.message.toString()))
            }
        }
}