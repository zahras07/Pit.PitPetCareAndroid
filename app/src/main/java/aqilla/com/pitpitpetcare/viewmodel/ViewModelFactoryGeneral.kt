package aqilla.com.pitpitpetcare.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import aqilla.com.pitpitpetcare.data.source.repository.GeneralRepository
import aqilla.com.pitpitpetcare.injection.Injection
import aqilla.com.pitpitpetcare.ui.viewmodel.GeneralViewModel

class ViewModelFactoryGeneral(private val generalRepository: GeneralRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GeneralViewModel::class.java)) {
            return GeneralViewModel(generalRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactoryGeneral? = null

        fun getInstance(context: Context): ViewModelFactoryGeneral =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactoryGeneral(Injection.providerGeneralRepository())
            }.also { instance = it }
    }
}