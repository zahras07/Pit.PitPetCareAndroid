package aqilla.com.pitpitpetcare.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import aqilla.com.pitpitpetcare.data.source.repository.AnimalRepository
import aqilla.com.pitpitpetcare.data.source.repository.ResultProcess
import aqilla.com.pitpitpetcare.data.source.response.AnimalResponse
import aqilla.com.pitpitpetcare.data.source.response.JenisHewanResponse
import aqilla.com.pitpitpetcare.data.source.response.PostPutDelAnimalResponse

class AnimalViewModel(private val animalRepository: AnimalRepository) : ViewModel() {
    fun animal(auth: String, customerId: Int): LiveData<ResultProcess<AnimalResponse>> {
        return animalRepository.animal(auth, customerId)
    }

    fun createAnimal(
        auth: String,
        namaHewan: String,
        jenisHewan: Int,
        umur: String,
        berat: String
    ): LiveData<ResultProcess<PostPutDelAnimalResponse>> {
        return animalRepository.createAnimal(auth, namaHewan, jenisHewan, umur, berat)
    }

    fun jenisHewan(): LiveData<ResultProcess<JenisHewanResponse>> {
        return animalRepository.jenisHewan()
    }

}