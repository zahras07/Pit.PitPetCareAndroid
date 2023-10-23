package aqilla.com.pitpitpetcare.data.source.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class Layanan(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("nama_layanan")
    val layanan: String,
) : Parcelable