package aqilla.com.pitpitpetcare.data.source.response

import android.os.Parcelable
import aqilla.com.pitpitpetcare.data.source.model.JenisHewan
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class JenisHewanResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val results: List<JenisHewan>
) : Parcelable