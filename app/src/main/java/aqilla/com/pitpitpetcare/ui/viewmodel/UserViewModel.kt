package aqilla.com.pitpitpetcare.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import aqilla.com.pitpitpetcare.data.source.repository.ResultProcess
import aqilla.com.pitpitpetcare.data.source.repository.UserRepository
import aqilla.com.pitpitpetcare.data.source.response.CustomerResponse
import aqilla.com.pitpitpetcare.data.source.response.LoginResponse
import aqilla.com.pitpitpetcare.data.source.response.RegisterResponse
import aqilla.com.pitpitpetcare.data.source.response.UserResponse
import okhttp3.MultipartBody

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun login(email: String, password: String): LiveData<ResultProcess<LoginResponse>> {
        return userRepository.login(email, password)
    }

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<ResultProcess<RegisterResponse>> {
        return userRepository.register(name, email, password)
    }

    fun updateDataCustomer(
        auth: String,
        customerId: Int,
        customerName: String,
        address: String,
        phoneNumber: String
    ): LiveData<ResultProcess<CustomerResponse>> {
        return userRepository.updateDataCustomer(
            auth,
            customerId,
            customerName,
            address,
            phoneNumber
        )
    }

    fun user(
        auth: String,
    ): LiveData<ResultProcess<UserResponse>> {
        return userRepository.user(
            auth,
        )
    }

    fun changePassword(
        auth: String,
        current_password: String,
        new_password: String,
        confirm_password: String,
    ): LiveData<ResultProcess<CustomerResponse>> {
        return userRepository.changePassword(
            auth,
            current_password, new_password, confirm_password
        )
    }

    fun changeEmail(
        auth: String,
        new_email: String,
        password: String,
    ): LiveData<ResultProcess<CustomerResponse>> {
        return userRepository.changeEmail(
            auth, new_email, password
        )
    }

    fun changePhotoProfileCustomer(
        auth: String,
        pelangganId: Int,
        foto: MultipartBody.Part,
    ): LiveData<ResultProcess<CustomerResponse>> {
        return userRepository.changePhotoProfileCustomer(
            auth, pelangganId, foto
        )
    }
}