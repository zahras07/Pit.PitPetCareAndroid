package aqilla.com.pitpitpetcare.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import aqilla.com.pitpitpetcare.BuildConfig
import aqilla.com.pitpitpetcare.R
import aqilla.com.pitpitpetcare.databinding.ActivityDetailImageBinding
import com.bumptech.glide.Glide

class DetailImageActivity : AppCompatActivity() {

    private lateinit var detailImageBinding: ActivityDetailImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailImageBinding = ActivityDetailImageBinding.inflate(layoutInflater)
        setContentView(detailImageBinding.root)

        supportActionBar?.title = intent.getStringExtra("title")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Glide.with(this).load(BuildConfig.BASE_URL_ASSET + intent.getStringExtra("data"))
            .into(detailImageBinding.image)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Handle the Up button click (Kembali ke aktivitas sebelumnya)
                onBackPressed() // Ini akan memanggil metode onBackPressed() secara otomatis
                return true
            }
            // Tambahan penanganan item lainnya jika diperlukan
        }
        return super.onOptionsItemSelected(item)
    }
}