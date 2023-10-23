package aqilla.com.pitpitpetcare.data.source.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import aqilla.com.pitpitpetcare.api.ApiConfig
import aqilla.com.pitpitpetcare.data.source.response.CustomerResponse
import aqilla.com.pitpitpetcare.data.source.response.LoginResponse
import aqilla.com.pitpitpetcare.data.source.response.RegisterResponse
import aqilla.com.pitpitpetcare.data.source.response.UserResponse
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.http.Field
import retrofit2.http.Header

class UserRepository(private val apiConfig: ApiConfig) {

    fun login(email: String, password: String): LiveData<ResultProcess<LoginResponse>> = liveData {
        emit(ResultProcess.Loading)

        try {
            val response = apiConfig.getUserService().login(email, password)

            if (response.isSuccessful){
                emit(ResultProcess.Success(response.body()!!))
            }else{
                // Tangani kode status selain 2xx
                when (response.code()) {
                    422 -> {
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
                    // Tambahkan penanganan kode status lain jika diperlukan
                    else -> {

                        emit(ResultProcess.Error("Ada Error"))
                    }
                }
            }

        } catch (e: Exception) {
            Log.e("Login Log : ", "OnFailure" + e.message.toString())
            emit(ResultProcess.Error(e.message.toString()))
        }
    }

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<ResultProcess<RegisterResponse>> = liveData {
        try {
            val response = apiConfig.getUserService().register(name, email, password)
            if (response.isSuccessful){
                emit(ResultProcess.Success(response.body()!!))
            }else{
                // Tangani kode status selain 2xx
                when (response.code()) {
                    422 -> {
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
                    // Tambahkan penanganan kode status lain jika diperlukan
                    else -> {

                        emit(ResultProcess.Error("Ada Error"))
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("Register Log ", "OnFailure " + e.message.toString())
            emit(ResultProcess.Error(e.message.toString()))
        }
    }

    fun updateDataCustomer(
        auth: String,
        customerId: Int,
        customerName: String,
        address: String,
        phoneNumber: String
    ): LiveData<ResultProcess<CustomerResponse>> = liveData {
        try {
            val response = apiConfig.getUserService()
                .updateDataCustomer("Bearer $auth", customerId, customerName, address, phoneNumber)
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
            Log.e("Register Log ", "OnFailure " + e.message.toString())
            emit(ResultProcess.Error(e.message.toString()))
        }
    }

    fun user(
        auth: String
    ): LiveData<ResultProcess<UserResponse>> = liveData {
        try {
            val response = apiConfig.getUserService()
                .user("Bearer $auth")
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
            Log.e("Register Log ", "OnFailure " + e.message.toString())
            emit(ResultProcess.Error(e.message.toString()))
        }
    }

    fun changePassword(
        auth: String,
        current_password: String,
        new_password: String,
        confirm_password: String,
    ): LiveData<ResultProcess<CustomerResponse>> = liveData {
        try {
            val response = apiConfig.getUserService()
                .changePassword("Bearer $auth", current_password, new_password, confirm_password)
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
            Log.e("Register Log ", "OnFailure " + e.message.toString())
            emit(ResultProcess.Error(e.message.toString()))
        }
    }

    fun changeEmail(
        auth: String,
        new_email: String,
        password: String,
    ): LiveData<ResultProcess<CustomerResponse>> = liveData {
        try {
            val response = apiConfig.getUserService()
                .changeEmail("Bearer $auth", new_email, password)
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
            Log.e("Register Log ", "OnFailure " + e.message.toString())
            emit(ResultProcess.Error(e.message.toString()))
        }
    }

    fun changePhotoProfileCustomer(
        auth: String,
        pelangganId: Int,
        foto: MultipartBody.Part,
    ): LiveData<ResultProcess<CustomerResponse>> = liveData {
        try {
            val response = apiConfig.getUserService()
                .changePhotoProfileCustomer("Bearer $auth", pelangganId, foto)
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
            Log.e("Register Log ", "OnFailure " + e.message.toString())
            emit(ResultProcess.Error(e.message.toString()))
        }
    }
}