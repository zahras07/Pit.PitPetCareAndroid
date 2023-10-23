package aqilla.com.pitpitpetcare.data.source.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScheduleDoctorItem(

    @field:SerializedName("jam_mulai")
    val jamMulai: String? = null,

    @field:SerializedName("keterangan")
    val keterangan: String? = null,

    @field:SerializedName("dokter")
    val dokter: Doctor? = null,

    @field:SerializedName("id_jadwal")
    val idJadwal: String? = null,

    @field:SerializedName("jam_selesai")
    val jamSelesai: String? = null,

    @field:SerializedName("day")
    val day: String? = null,

    @field:SerializedName("status")
    val status: String? = null
) : Parcelable