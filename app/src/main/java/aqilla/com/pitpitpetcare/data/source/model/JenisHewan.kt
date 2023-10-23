package aqilla.com.pitpitpetcare.data.source.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class JenisHewan(

    @field:SerializedName("id_jenis_hewan")
    val idJenisJewan: Int,

    @field:SerializedName("nama_jenis")
    val namaJenis: String,

    @field:SerializedName("deskripsi")
    val deskripsi: String

) : Parcelable