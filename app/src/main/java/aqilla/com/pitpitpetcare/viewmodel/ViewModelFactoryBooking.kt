package aqilla.com.pitpitpetcare.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import aqilla.com.pitpitpetcare.data.source.repository.BookingRepository
import aqilla.com.pitpitpetcare.injection.Injection
import aqilla.com.pitpitpetcare.ui.viewmodel.BookingViewModel

class ViewModelFactoryBooking(private val bookingRepository: BookingRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            return BookingViewModel(bookingRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class " + modelClass.name)
    }

    companion object {

        @Volatile
        private var instance: ViewModelFactoryBooking? = null

        fun getInstance(context: Context): ViewModelFactoryBooking =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactoryBooking(Injection.providerBookingRepository())
            }.also { instance = it }
    }
}