package aqilla.com.pitpitpetcare.data.source.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Paket(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("layanan_id")
    val layanan_id: Int,

    @field:SerializedName("nama_paket")
    val nama_paket: String,

    @field:SerializedName("harga")
    val harga: String,

    @field:SerializedName("deskripsi")
    val deskripsi: String,

    @field:SerializedName("created_at")
    val created_at: String,

    @field:SerializedName("updated_at")
    val updated_at: String,
) : Parcelable