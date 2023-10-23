package aqilla.com.pitpitpetcare.data.source.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Doctor(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("foto")
    val foto: String? = null,

    @field:SerializedName("no_rek")
    val noRek: String? = null,

    @field:SerializedName("tgl_rek")
    val tglRek: String? = null,

    @field:SerializedName("nama_dokter")
    val namaDokter: String? = null,

    @field:SerializedName("alamat_praktek")
    val alamatPraktek: String? = null,

    @field:SerializedName("masa_berlaku")
    val masaBerlaku: String? = null,

    @field:SerializedName("ttl")
    val ttl: String? = null,

    @field:SerializedName("certificate_photo")
    val certificate_photo: String? = null,

    @field:SerializedName("jadwal")
    val scheduleDoctor: List<ScheduleDoctorItem>,
) : Parcelable