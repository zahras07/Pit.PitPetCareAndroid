package aqilla.com.pitpitpetcare.data.source.response

import aqilla.com.pitpitpetcare.data.source.model.Doctor
import com.google.gson.annotations.SerializedName

data class DetailDoctorResponse(

    @field:SerializedName("data")
    val data: Doctor,

    @field:SerializedName("message")
    val message: String? = null
)