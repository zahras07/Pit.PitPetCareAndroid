package aqilla.com.pitpitpetcare.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import aqilla.com.pitpitpetcare.data.source.repository.UserRepository
import aqilla.com.pitpitpetcare.injection.Injection
import aqilla.com.pitpitpetcare.ui.viewmodel.UserViewModel

class ViewModelFactoryUser(private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class " + modelClass.name)
    }

    companion object {

        @Volatile
        private var instance: ViewModelFactoryUser? = null

        fun getInstance(context: Context): ViewModelFactoryUser = instance ?: synchronized(this) {
            instance ?: ViewModelFactoryUser(Injection.providerUserRepository())
        }.also { instance = it }
    }
}