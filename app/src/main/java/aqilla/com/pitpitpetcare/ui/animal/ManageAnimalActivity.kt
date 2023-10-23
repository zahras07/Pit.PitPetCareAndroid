package aqilla.com.pitpitpetcare.ui.animal

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import aqilla.com.pitpitpetcare.R
import aqilla.com.pitpitpetcare.data.source.repository.ResultProcess
import aqilla.com.pitpitpetcare.data.source.response.LoginResponse
import aqilla.com.pitpitpetcare.databinding.ActivityManageAnimalBinding
import aqilla.com.pitpitpetcare.ui.auth.LoginActivity
import aqilla.com.pitpitpetcare.ui.viewmodel.AnimalViewModel
import aqilla.com.pitpitpetcare.utils.MySharedPreferences
import aqilla.com.pitpitpetcare.utils.MyToast
import aqilla.com.pitpitpetcare.utils.ShowLoadingDialog
import aqilla.com.pitpitpetcare.viewmodel.ViewModelFactoryAnimal

class ManageAnimalActivity : AppCompatActivity() {

    private lateinit var manageAnimalBinding: ActivityManageAnimalBinding

    private lateinit var loginResponse: LoginResponse
    private lateinit var mySharedPreferences: MySharedPreferences

    private val factoryAnimal = ViewModelFactoryAnimal.getInstance(this)
    private val animalViewModel: AnimalViewModel by viewModels { factoryAnimal }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manageAnimalBinding = ActivityManageAnimalBinding.inflate(layoutInflater)
        setContentView(manageAnimalBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mySharedPreferences = MySharedPreferences(this)

        loginResponse = mySharedPreferences.getAuthData()

        loadJenisHewan()

        manageAnimalBinding.animalName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) {
                    manageAnimalBinding.layoutValueAnimalName.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        manageAnimalBinding.animalAge.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) {
                    manageAnimalBinding.layoutValueAge.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                R.style.BottomSheetDialogStyle
            }
        })

        manageAnimalBinding.animalWeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) {
                    manageAnimalBinding.layoutValueWeight.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })


        manageAnimalBinding.createAnimal.setOnClickListener {


            if (manageAnimalBinding.animalName.text.toString().isEmpty()) {
                manageAnimalBinding.layoutValueAnimalName.isErrorEnabled = true
                manageAnimalBinding.layoutValueAnimalName.error = "Nama hewan belum diisi"
                return@setOnClickListener
            }


            if (jenisHewanId == null) {
                manageAnimalBinding.layoutValueAnimalType.isErrorEnabled = true
                manageAnimalBinding.layoutValueAnimalType.error = "Jenis Hewan belum dipilih"
                return@setOnClickListener
            }

            if (manageAnimalBinding.animalAge.text.toString().isEmpty()) {
                manageAnimalBinding.layoutValueAge.isErrorEnabled = true
                manageAnimalBinding.layoutValueAge.error = "Umur hewan belum diisi"
                return@setOnClickListener
            }

            if (manageAnimalBinding.animalWeight.text.toString().isEmpty()) {
                manageAnimalBinding.layoutValueWeight.isErrorEnabled = true
                manageAnimalBinding.layoutValueWeight.error = "Berat hewan belum diisi"
                return@setOnClickListener
            }


            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle(resources.getString(R.string.tambah_hewan))
            builder.setMessage(getString(R.string.apakah_data_yang_dimasukkan_sudah_benar))
            builder.setPositiveButton("Sudah") { _, _ ->
                createAnimal()
            }

            builder.setNegativeButton("Kembali") { dialog, _ ->

                dialog.dismiss()

            }

            builder.show()

        }

    }

    private fun createAnimal() {
        animalViewModel.createAnimal(
            loginResponse.token,
            manageAnimalBinding.animalName.text.toString(),
            jenisHewanId!!,
            manageAnimalBinding.animalAge.text.toString(),
            manageAnimalBinding.animalWeight.text.toString()
        ).observe(this) { result ->


            if (result != null) {
                when (result) {
                    is ResultProcess.Loading -> {
                        loadingDialog = ShowLoadingDialog()
                        loadingDialog.startDialog(getString(R.string.please_wait), this)
                    }

                    is ResultProcess.Success -> {
                        loadingDialog.dismissDialog()
                        val myToast = MyToast()

                        myToast.showToast(
                            this,
                            result.data.message,
                            ContextCompat.getColor(applicationContext, R.color.green)
                        )
                        finish()
                    }

                    is ResultProcess.Error -> {
                        loadingDialog.dismissDialog()
                        if (result.error == "401") {
                            mySharedPreferences.logout()

                            val myToast = MyToast()
                            myToast.showToast(
                                this,
                                "Sesi telah habis, Silahkan login kembali",
                                ContextCompat.getColor(this, R.color.red)
                            )

                            Intent(this, LoginActivity::class.java).also {
                                it.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(it)
                                finish()
                            }
                        } else {
                            val myToast = MyToast()
                            myToast.showToast(
                                this,
                                result.error,
                                ContextCompat.getColor(this, R.color.red)
                            )
                        }

                    }
                }
            }
        }
    }

    private fun loadJenisHewan() {
        animalViewModel.jenisHewan()
            .observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultProcess.Loading -> {

                        }

                        is ResultProcess.Success -> {
                            val arrayJenisHewan = ArrayList<String>()

                            result.data.results.forEach {
                                arrayJenisHewan.add(it.namaJenis)
                            }

                            val adapterJenisHewan = ArrayAdapter(
                                applicationContext, android.R.layout.simple_spinner_dropdown_item,
                                arrayJenisHewan
                            )


                            manageAnimalBinding.jenisHewan.setAdapter(adapterJenisHewan)

                            manageAnimalBinding.jenisHewan.onItemClickListener =
                                AdapterView.OnItemClickListener { _, _, position, _ ->

                                    jenisHewanId = result.data.results[position].idJenisJewan
                                    manageAnimalBinding.layoutValueAnimalType.isErrorEnabled = false
                                }

                        }

                        is ResultProcess.Error -> {
                            if (result.error == "401") {
                                mySharedPreferences.logout()

                                val myToast = MyToast()
                                myToast.showToast(
                                    this,
                                    "Sesi telah habis, Silahkan login kembali",
                                    ContextCompat.getColor(this, R.color.red)
                                )

                                Intent(this, LoginActivity::class.java).also {
                                    it.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(it)
                                    finish()
                                }
                            } else {
                                val myToast = MyToast()
                                myToast.showToast(
                                    this,
                                    result.error,
                                    ContextCompat.getColor(this, R.color.red)
                                )
                            }


                        }
                    }
                }
            }
    }

    companion object {
        private lateinit var loadingDialog: ShowLoadingDialog
        private var jenisHewanId: Int? = null
    }
}