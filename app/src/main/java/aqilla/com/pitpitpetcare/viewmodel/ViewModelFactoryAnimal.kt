package aqilla.com.pitpitpetcare.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import aqilla.com.pitpitpetcare.data.source.repository.AnimalRepository
import aqilla.com.pitpitpetcare.injection.Injection
import aqilla.com.pitpitpetcare.ui.viewmodel.AnimalViewModel

class ViewModelFactoryAnimal(private val animalRepository: AnimalRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnimalViewModel::class.java)) {
            return AnimalViewModel(animalRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactoryAnimal? = null

        fun getInstance(context: Context): ViewModelFactoryAnimal = instance ?: synchronized(this) {
            instance ?: ViewModelFactoryAnimal(Injection.providerAnimalRepository())
        }.also { instance = it }
    }
}