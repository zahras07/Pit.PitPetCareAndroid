package aqilla.com.pitpitpetcare.ui.dokter

import android.content.Intent
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import aqilla.com.pitpitpetcare.BuildConfig
import aqilla.com.pitpitpetcare.R
import aqilla.com.pitpitpetcare.adapter.AdapterScheduleDoctor
import aqilla.com.pitpitpetcare.data.source.model.Doctor
import aqilla.com.pitpitpetcare.databinding.ActivityDetailDokterBinding
import aqilla.com.pitpitpetcare.ui.DetailImageActivity
import aqilla.com.pitpitpetcare.utils.MyToast
import com.bumptech.glide.Glide

class DetailDokterActivity : AppCompatActivity() {

    private lateinit var detailDokterBinding: ActivityDetailDokterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailDokterBinding = ActivityDetailDokterBinding.inflate(layoutInflater)
        setContentView(detailDokterBinding.root)

        supportActionBar?.title = resources.getString(R.string.detail_dokter)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val doctor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("data", Doctor::class.java)
        } else {
            intent.getParcelableExtra<Doctor>("data") as Doctor
        }

        detailDokterBinding.doctorName.text = doctor!!.namaDokter
        Glide.with(this).load(BuildConfig.BASE_URL_ASSET + doctor.foto)
            .placeholder(R.drawable.default_profile_user).error(R.drawable.default_profile_user)
            .into(detailDokterBinding.doctorProfile)
        detailDokterBinding.alamatPrakter.text = doctor.alamatPraktek
        detailDokterBinding.registerNumber.text = doctor.noRek
        detailDokterBinding.registerDate.text = doctor.tglRek
        detailDokterBinding.expiredDate.text = doctor.masaBerlaku

        //untuk memberian garis bawah pada teks
        detailDokterBinding.lihatSertifikat.paintFlags =
            detailDokterBinding.lihatSertifikat.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        if (doctor.scheduleDoctor.isEmpty()) {
            detailDokterBinding.emptySchedule.visibility = View.VISIBLE
        } else {
            detailDokterBinding.emptySchedule.visibility = View.GONE

            detailDokterBinding.rvJadwalPraktek.layoutManager = GridLayoutManager(
                this,
                1,
                GridLayoutManager.HORIZONTAL,
                false
            )


            val adapterScheduleDoctor = AdapterScheduleDoctor(doctor.scheduleDoctor)
            adapterScheduleDoctor.notifyDataSetChanged()

            detailDokterBinding.rvJadwalPraktek.adapter = adapterScheduleDoctor
        }

        if (doctor.certificate_photo != null) {
            detailDokterBinding.lihatSertifikat.setOnClickListener {
                Intent(this, DetailImageActivity::class.java).also {
                    it.putExtra("data", doctor.certificate_photo)
                    it.putExtra("title", resources.getString(R.string.surat_izin_praktek))
                    startActivity(it)
                }
            }
        } else {
            MyToast().showToast(
                this,
                getString(R.string.sertifikat_tidak_tersedia),
                ContextCompat.getColor(this, R.color.blue)
            )
        }

        detailDokterBinding.doctorProfile.setOnClickListener {
            Intent(this, DetailImageActivity::class.java).also {
                it.putExtra("data", doctor.foto)
                it.putExtra("title", resources.getString(R.string.profile))
                startActivity(it)
            }
        }
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