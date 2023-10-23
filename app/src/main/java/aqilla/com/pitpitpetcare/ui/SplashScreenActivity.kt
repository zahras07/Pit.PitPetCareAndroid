package aqilla.com.pitpitpetcare.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import aqilla.com.pitpitpetcare.R
import aqilla.com.pitpitpetcare.ui.auth.LoginActivity


class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        //Buat sebuah Handler dengan Looper dari thread utama (main thread)
        val handler = Handler(Looper.getMainLooper())


        // Kirimkan sebuah Runnable untuk dijalankan di thread utama setelah waktu delay
        handler.postDelayed( {
            // Your code here
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)

    }

    companion object {
        // Waktu delay dalam milidetik (misalnya, 2000 ms untuk 2 detik)
        private const val SPLASH_TIME_OUT: Long = 3000;
    }
}