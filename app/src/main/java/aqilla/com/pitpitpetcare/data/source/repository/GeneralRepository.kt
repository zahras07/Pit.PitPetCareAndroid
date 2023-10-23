package aqilla.com.pitpitpetcare.data.source.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import aqilla.com.pitpitpetcare.api.ApiConfig
import aqilla.com.pitpitpetcare.data.source.response.LayananResponse
import aqilla.com.pitpitpetcare.data.source.response.PaketResponse

class GeneralRepository(private val apiConfig: ApiConfig) {

    fun paket(layananId: Int): LiveData<ResultProcess<PaketResponse>> =
        liveData {
            emit(ResultProcess.Loading)

            try {
                val response = apiConfig.getGeneralService().paket(layananId)
                if (response.isSuccessful){
                    emit(ResultProcess.Success(response.body()!!))
                }else{
                    // Tangani kode status selain 2xx
                    emit(ResultProcess.Error("Permintaan tidak dapat diproses"))
                }
            } catch (e: Exception) {
                Log.e("General Log : ", "OnFailure" + e.message.toString())
                emit(ResultProcess.Error(e.message.toString()))
            }
        }

    fun layanan(): LiveData<ResultProcess<LayananResponse>> =
        liveData {
            emit(ResultProcess.Loading)

            try {
                val response = apiConfig.getGeneralService().layanan()
                if (response.isSuccessful){
                    emit(ResultProcess.Success(response.body()!!))
                }else{
                    // Tangani kode status selain 2xx
                    emit(ResultProcess.Error("Permintaan tidak dapat diproses"))
                }
            } catch (e: Exception) {
                Log.e("General Log : ", "OnFailure" + e.message.toString())
                emit(ResultProcess.Error(e.message.toString()))
            }
        }

    fun allPaket(): LiveData<ResultProcess<PaketResponse>> =
        liveData {
            emit(ResultProcess.Loading)

            try {
                val response = apiConfig.getGeneralService().allPaket()
                if (response.isSuccessful){
                    emit(ResultProcess.Success(response.body()!!))
                }else{
                    // Tangani kode status selain 2xx
                    emit(ResultProcess.Error("Permintaan tidak dapat diproses"))
                }
            } catch (e: Exception) {
                Log.e("General Log : ", "OnFailure" + e.message.toString())
                emit(ResultProcess.Error(e.message.toString()))
            }
        }

}