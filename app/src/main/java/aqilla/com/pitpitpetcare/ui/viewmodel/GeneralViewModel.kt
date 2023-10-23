package aqilla.com.pitpitpetcare.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import aqilla.com.pitpitpetcare.data.source.repository.GeneralRepository
import aqilla.com.pitpitpetcare.data.source.repository.ResultProcess
import aqilla.com.pitpitpetcare.data.source.response.LayananResponse
import aqilla.com.pitpitpetcare.data.source.response.PaketResponse

class GeneralViewModel(private val generalRepository: GeneralRepository) : ViewModel() {

    fun paket(layananId: Int): LiveData<ResultProcess<PaketResponse>> {
        return generalRepository.paket(layananId)
    }

    fun layanan(): LiveData<ResultProcess<LayananResponse>> {
        return generalRepository.layanan()
    }

    fun allPackage(): LiveData<ResultProcess<PaketResponse>> {
        return generalRepository.allPaket()
    }
}