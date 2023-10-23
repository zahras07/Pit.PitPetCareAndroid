package aqilla.com.pitpitpetcare.data.source.response

import android.os.Parcelable
import aqilla.com.pitpitpetcare.data.source.model.Booking
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class BookingResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: List<Booking>
) : Parcelable