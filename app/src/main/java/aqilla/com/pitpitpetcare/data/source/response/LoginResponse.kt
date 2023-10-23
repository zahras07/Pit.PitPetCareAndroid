package aqilla.com.pitpitpetcare.data.source.response

import android.os.Parcelable
import aqilla.com.pitpitpetcare.data.source.model.User
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("token")
    val token: String,

    @field:SerializedName("user")
    var dataUser: User
) : Parcelable