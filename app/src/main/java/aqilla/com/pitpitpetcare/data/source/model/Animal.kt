package aqilla.com.pitpitpetcare.data.source.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Animal(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("nama_hewan")
    val namaHewan: String,

    @field:SerializedName("umur")
    val umur: String? = null,

    @field:SerializedName("pelanggan_id")
    val pelangganId: Int,

    @field:SerializedName("jenis_hewan")
    val jenisHewan: String? = null,

    @field:SerializedName("berat")
    val berat: String? = null,

    @field:SerializedName("pelanggan_name")
    val pelangganName: String? = null
) : Parcelable