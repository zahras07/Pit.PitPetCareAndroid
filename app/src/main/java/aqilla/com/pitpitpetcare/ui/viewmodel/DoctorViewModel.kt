package aqilla.com.pitpitpetcare.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import aqilla.com.pitpitpetcare.data.source.repository.DoctorRepository
import aqilla.com.pitpitpetcare.data.source.repository.ResultProcess
import aqilla.com.pitpitpetcare.data.source.response.DetailDoctorResponse
import aqilla.com.pitpitpetcare.data.source.response.DoctorResponse
import aqilla.com.pitpitpetcare.data.source.response.ScheduleResponse

class DoctorViewModel(private val doctorRepository: DoctorRepository) : ViewModel() {

    fun doctor(auth: String): LiveData<ResultProcess<DoctorResponse>> {
        return doctorRepository.doctor(auth)
    }

    fun schedule(auth: String): LiveData<ResultProcess<ScheduleResponse>> {
        return doctorRepository.schedule(auth)
    }

    fun doctorDetail(auth: String, id: Int): LiveData<ResultProcess<DetailDoctorResponse>> {
        return doctorRepository.doctorDetail(auth, id)
    }

}