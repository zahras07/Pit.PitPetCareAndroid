package aqilla.com.pitpitpetcare.data.source.response

import android.os.Parcelable
import aqilla.com.pitpitpetcare.data.source.model.User
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val dataUser: User
) : Parcelable