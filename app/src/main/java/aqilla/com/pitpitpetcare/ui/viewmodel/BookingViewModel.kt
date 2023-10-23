package aqilla.com.pitpitpetcare.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import aqilla.com.pitpitpetcare.data.source.repository.BookingRepository
import aqilla.com.pitpitpetcare.data.source.repository.ResultProcess
import aqilla.com.pitpitpetcare.data.source.response.BookingResponse
import aqilla.com.pitpitpetcare.data.source.response.PostPutDelBookingResponse

class BookingViewModel(private val bookingRepository: BookingRepository) : ViewModel() {

    //create booking
    fun createBooking(
        auth: String,
        dokter_id: Int?,
        pelanggan_id: Int,
        hewan_id: Int,
        layanan_id: Int,
        paket_id: Int,
        booking_date: String,
        delivery_time: String,
        pickup_time: String,
        total: String
    ): LiveData<ResultProcess<PostPutDelBookingResponse>> {
        return bookingRepository.createBooking(
            auth,
            dokter_id,
            pelanggan_id,
            hewan_id,
            layanan_id,
            paket_id,
            booking_date,
            delivery_time,
            pickup_time,
            total
        )
    }

    //show booking
    fun booking(auth: String): LiveData<ResultProcess<BookingResponse>> {
        return bookingRepository.booking(auth)
    }

    //cancel booking
    fun cancelBooking(
        auth: String,
        transactionId: Int
    ): LiveData<ResultProcess<PostPutDelBookingResponse>> {
        return bookingRepository.cancelbooking(auth, transactionId)
    }
}