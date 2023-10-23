package aqilla.com.pitpitpetcare.injection

import aqilla.com.pitpitpetcare.api.ApiConfig
import aqilla.com.pitpitpetcare.data.source.repository.AnimalRepository
import aqilla.com.pitpitpetcare.data.source.repository.BookingRepository
import aqilla.com.pitpitpetcare.data.source.repository.DoctorRepository
import aqilla.com.pitpitpetcare.data.source.repository.GeneralRepository
import aqilla.com.pitpitpetcare.data.source.repository.UserRepository

object Injection {

    fun providerUserRepository(): UserRepository {
        val apiConfig = ApiConfig
        return UserRepository(apiConfig)
    }

    fun providerDoctorRepository(): DoctorRepository {
        val apiConfig = ApiConfig
        return DoctorRepository(apiConfig)
    }

    fun providerAnimalRepository(): AnimalRepository {
        val apiConfig = ApiConfig
        return AnimalRepository(apiConfig)
    }

    fun providerGeneralRepository(): GeneralRepository {
        val apiConfig = ApiConfig
        return GeneralRepository(apiConfig)
    }

    fun providerBookingRepository(): BookingRepository {
        val apiConfig = ApiConfig
        return BookingRepository(apiConfig)
    }
}