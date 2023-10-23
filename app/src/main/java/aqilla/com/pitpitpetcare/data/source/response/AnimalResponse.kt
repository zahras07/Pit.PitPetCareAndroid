package aqilla.com.pitpitpetcare.data.source.response

import android.os.Parcelable
import aqilla.com.pitpitpetcare.data.source.model.Animal
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnimalResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val results: List<Animal>
) : Parcelable