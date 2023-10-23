package aqilla.com.pitpitpetcare.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import aqilla.com.pitpitpetcare.data.source.repository.DoctorRepository
import aqilla.com.pitpitpetcare.injection.Injection
import aqilla.com.pitpitpetcare.ui.viewmodel.DoctorViewModel

class ViewModelFactoryDoctor(private val doctorRepository: DoctorRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DoctorViewModel::class.java)) {
            return DoctorViewModel(doctorRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class " + modelClass.name)
    }

    companion object {

        @Volatile
        private var instance: ViewModelFactoryDoctor? = null

        fun getInstance(context: Context): ViewModelFactoryDoctor = instance ?: synchronized(this) {
            instance ?: ViewModelFactoryDoctor(Injection.providerDoctorRepository())
        }.also { instance = it }
    }
}