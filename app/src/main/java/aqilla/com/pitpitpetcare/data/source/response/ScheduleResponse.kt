package aqilla.com.pitpitpetcare.data.source.response

import aqilla.com.pitpitpetcare.data.source.model.Schedule
import com.google.gson.annotations.SerializedName

data class ScheduleResponse(

	@field:SerializedName("data")
	val data: List<Schedule>,

	@field:SerializedName("message")
	val message: String? = null
)