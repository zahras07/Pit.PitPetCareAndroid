package aqilla.com.pitpitpetcare.data.source.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transaction(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("pelanggan_id")
    val pelanggan_id: Int,

    @field:SerializedName("hewan_id")
    val hewan_id: String,

    @field:SerializedName("layanan_id")
    val layanan_id: String,

    @field:SerializedName("paket_id")
    val paket_id: String,

    @field:SerializedName("tgl_transaksi")
    val tgl_transaksi: String,

    @field:SerializedName("total")
    val total: String,

    @field:SerializedName("status")
    val status: String,

    ) : Parcelable