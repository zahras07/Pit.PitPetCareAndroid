package aqilla.com.pitpitpetcare.data.source.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import aqilla.com.pitpitpetcare.api.ApiConfig
import aqilla.com.pitpitpetcare.data.source.response.AnimalResponse
import aqilla.com.pitpitpetcare.data.source.response.JenisHewanResponse
import aqilla.com.pitpitpetcare.data.source.response.PostPutDelAnimalResponse
import org.json.JSONObject

class AnimalRepository(private val apiConfig: ApiConfig) {

    fun animal(auth: String, customerId: Int): LiveData<ResultProcess<AnimalResponse>> =
        liveData {
            emit(ResultProcess.Loading)

            try {
                val response = apiConfig.getAnimalService().animal("Bearer $auth", customerId)
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
                Log.e("Animal Log : ", "OnFailure" + e.message.toString())
                emit(ResultProcess.Error(e.message.toString()))
            }
        }

    fun createAnimal(auth: String, namaHewan: String, jenisHewan: Int, umur: String, berat: String): LiveData<ResultProcess<PostPutDelAnimalResponse>> =
        liveData {
            emit(ResultProcess.Loading)

            try {
                val response = apiConfig.getAnimalService().createAnimal("Bearer $auth", namaHewan, jenisHewan, umur, berat)
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
                Log.e("Animal Log : ", "OnFailure" + e.message.toString())
                emit(ResultProcess.Error(e.message.toString()))
            }
        }


    fun jenisHewan(): LiveData<ResultProcess<JenisHewanResponse>> =
        liveData {
            emit(ResultProcess.Loading)

            try {
                val response = apiConfig.getAnimalService().jenisHewan()
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
                Log.e("Animal Log : ", "OnFailure" + e.message.toString())
                emit(ResultProcess.Error(e.message.toString()))
            }
        }
}