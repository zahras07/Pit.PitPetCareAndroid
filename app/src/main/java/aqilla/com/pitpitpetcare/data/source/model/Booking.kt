package aqilla.com.pitpitpetcare.data.source.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Booking(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("pelanggan")
    val pelanggan: Customer,

    @field:SerializedName("hewan")
    val animal: Animal,

    @field:SerializedName("layanan")
    val layanan: Layanan,

    @field:SerializedName("dokter")
    val dokter: Doctor?,

    @field:SerializedName("paket")
    val paket: Paket,

    @field:SerializedName("tgl_transaksi")
    val tgl_transaksi: String,

    @field:SerializedName("total")
    val total: String,

    @field:SerializedName("nomor_antrian")
    val nomor_antrian: Int,

    @field:SerializedName("status")
    val status: String
) : Parcelable