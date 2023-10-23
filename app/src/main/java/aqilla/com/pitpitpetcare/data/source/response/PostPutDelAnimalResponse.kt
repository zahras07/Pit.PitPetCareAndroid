package aqilla.com.pitpitpetcare.data.source.response

import android.os.Parcelable
import aqilla.com.pitpitpetcare.data.source.model.Animal
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class PostPutDelAnimalResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: Animal
) : Parcelable