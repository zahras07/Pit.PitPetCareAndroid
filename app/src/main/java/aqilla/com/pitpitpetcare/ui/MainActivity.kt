package aqilla.com.pitpitpetcare.ui

import android.graphics.drawable.ColorDrawable
import aqilla.com.pitpitpetcare.ui.fragment.HomeFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import aqilla.com.pitpitpetcare.ui.fragment.BookingFragment
import aqilla.com.pitpitpetcare.ui.fragment.HewanFragment
import aqilla.com.pitpitpetcare.ui.fragment.ProfilFragment
import aqilla.com.pitpitpetcare.R
import aqilla.com.pitpitpetcare.data.source.response.LoginResponse
import aqilla.com.pitpitpetcare.databinding.ActivityMainBinding
import aqilla.com.pitpitpetcare.utils.MySharedPreferences

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var loginResponse: LoginResponse

    private lateinit var mySharedPreferences: MySharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mySharedPreferences = MySharedPreferences(this)

        loginResponse = mySharedPreferences.getAuthData()

        supportActionBar?.apply { setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(applicationContext, R.color.brown))) }

        binding.bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> loadFragment(HomeFragment())
                R.id.bottom_booking -> loadFragment(BookingFragment())
                R.id.bottom_hewan -> loadFragment(HewanFragment())
                R.id.bottom_profil -> loadFragment(ProfilFragment())
            }
            true
        }

        // Load initial fragment (Misal: aqilla.com.pitpitpetcare.ui.fragment.HomeFragment)
        loadFragment(HomeFragment())
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.frameContainer.id, fragment)
            .commit()
    }
}