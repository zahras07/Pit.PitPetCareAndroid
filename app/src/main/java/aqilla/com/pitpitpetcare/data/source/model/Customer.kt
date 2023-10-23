package aqilla.com.pitpitpetcare.data.source.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Customer(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("user_id")
    val user_id: Int,

    @field:SerializedName("nama_pelanggan")
    val nama_pelanggan: String,

    @field:SerializedName("alamat")
    val alamat: String,

    @field:SerializedName("telepon")
    val telepon: String,

    @field:SerializedName("foto")
    val foto: String,
) : Parcelable