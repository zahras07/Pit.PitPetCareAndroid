package aqilla.com.pitpitpetcare.ui.booking

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import aqilla.com.pitpitpetcare.R
import aqilla.com.pitpitpetcare.data.source.repository.ResultProcess
import aqilla.com.pitpitpetcare.data.source.response.LoginResponse
import aqilla.com.pitpitpetcare.databinding.ActivityBookingBinding
import aqilla.com.pitpitpetcare.databinding.LayoutBottomDateBinding
import aqilla.com.pitpitpetcare.ui.auth.LoginActivity
import aqilla.com.pitpitpetcare.ui.viewmodel.AnimalViewModel
import aqilla.com.pitpitpetcare.ui.viewmodel.BookingViewModel
import aqilla.com.pitpitpetcare.ui.viewmodel.DoctorViewModel
import aqilla.com.pitpitpetcare.ui.viewmodel.GeneralViewModel
import aqilla.com.pitpitpetcare.utils.CustomDatePickerView
import aqilla.com.pitpitpetcare.utils.MySharedPreferences
import aqilla.com.pitpitpetcare.utils.MyToast
import aqilla.com.pitpitpetcare.utils.ShowLoadingDialog
import aqilla.com.pitpitpetcare.viewmodel.ViewModelFactoryAnimal
import aqilla.com.pitpitpetcare.viewmodel.ViewModelFactoryBooking
import aqilla.com.pitpitpetcare.viewmodel.ViewModelFactoryDoctor
import aqilla.com.pitpitpetcare.viewmodel.ViewModelFactoryGeneral
import com.explorindo.tokonedwiplseller.utils.ConvertCurrency
import com.explorindo.tokonedwiplseller.utils.ConvertFormatDate
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookingActivity : AppCompatActivity() {

    private lateinit var activityBookingBinding: ActivityBookingBinding

    private lateinit var layoutBottomDateBinding: LayoutBottomDateBinding

    private lateinit var sheetDialog: BottomSheetDialog

    private val factoryAnimal = ViewModelFactoryAnimal.getInstance(this)
    private val animalViewModel: AnimalViewModel by viewModels { factoryAnimal }

    private val factoryGeneral = ViewModelFactoryGeneral.getInstance(this)
    private val generalViewModel: GeneralViewModel by viewModels { factoryGeneral }

    private val factoryDoctor = ViewModelFactoryDoctor.getInstance(this)
    private val doctorViewModel: DoctorViewModel by viewModels { factoryDoctor }

    private val factoryBooking = ViewModelFactoryBooking.getInstance(this)
    private val bookingViewModel: BookingViewModel by viewModels { factoryBooking }


    private var mySharedPreferences = MySharedPreferences(this)

    private lateinit var loginResponse: LoginResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBookingBinding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(activityBookingBinding.root)

        mySharedPreferences = MySharedPreferences(this)

        loginResponse = mySharedPreferences.getAuthData()

        activityBookingBinding.bookingDate.setOnClickListener {
            showLayoutDate(activityBookingBinding.bookingDate)
        }
        loadAnimalUser()

        loadLayanan()

        activityBookingBinding.deliveryTime.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) {
                    activityBookingBinding.layoutDeliveryTime.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        activityBookingBinding.pickUpTime.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) {
                    activityBookingBinding.layoutPickUpTime.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        activityBookingBinding.bookingNow.setOnClickListener {

            if (layananId == null) {
                activityBookingBinding.layoutLayanan.isErrorEnabled = true
                activityBookingBinding.layoutLayanan.error = "Layanan belum dipilih"
                return@setOnClickListener
            }


            if (paketId == null) {
                activityBookingBinding.layoutPaket.isErrorEnabled = true
                activityBookingBinding.layoutPaket.error = "Paket Layanan belum dipilih"
                return@setOnClickListener
            }

            if (animalId == null) {
                activityBookingBinding.layoutAnimal.isErrorEnabled = true
                activityBookingBinding.layoutAnimal.error = "Hewan belum dipilih"
                return@setOnClickListener
            }

            if (layananId == 1) {
                if (doctorId == null) {
                    activityBookingBinding.layoutDoctor.isErrorEnabled = true
                    activityBookingBinding.layoutDoctor.error = "Dokter belum dipilih"
                    return@setOnClickListener
                }
            }

            if (activityBookingBinding.bookingDate.text.toString().isEmpty()) {
                activityBookingBinding.layoutBookingDate.isErrorEnabled = true
                activityBookingBinding.layoutBookingDate.error = "Tanggal booking belum dipilih"
                return@setOnClickListener
            }

            if (activityBookingBinding.deliveryTime.text.toString().isEmpty()) {
                activityBookingBinding.layoutDeliveryTime.isErrorEnabled = true
                activityBookingBinding.layoutDeliveryTime.error = "Jam antar belum dipilih"
                return@setOnClickListener
            }

            if (activityBookingBinding.pickUpTime.text.toString().isEmpty()) {
                activityBookingBinding.layoutPickUpTime.isErrorEnabled = true
                activityBookingBinding.layoutPickUpTime.error = "Jam jemput belum dipilih"
                return@setOnClickListener
            }


            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle(resources.getString(R.string.booking))
            builder.setMessage("Apakah data yang dimasukkan sudah benar?")
            builder.setPositiveButton("Ya") { _, _ ->


                createBooking()
            }

            builder.setNegativeButton("Batal") { dialog, _ ->

                dialog.dismiss()

            }

            builder.show()
        }


        activityBookingBinding.pickUpTime.setOnClickListener {
            showTimePickerDialog(activityBookingBinding.pickUpTime)
        }

        activityBookingBinding.deliveryTime.setOnClickListener {
            showTimePickerDialog(activityBookingBinding.deliveryTime)
        }
    }

    private fun createBooking() {
        bookingViewModel.createBooking(
            loginResponse.token,
            doctorId,
            loginResponse.dataUser.pelanggan!!.id,
            animalId!!,
            layananId!!,
            paketId!!,
            activityBookingBinding.bookingDate.text.toString(),
            activityBookingBinding.deliveryTime.text.toString(),
            activityBookingBinding.pickUpTime.text.toString(),
            priceService
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

    private fun loadPaket(layananId: Int) {
        generalViewModel.paket(layananId)
            .observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultProcess.Loading -> {

                        }

                        is ResultProcess.Success -> {
                            val arrayPaket = ArrayList<String>()

                            if(result.data.results.isEmpty()){
                                val myToast = MyToast()
                                myToast.showToast(
                                    this,
                                    "Paket layanan tidak tersedia",
                                    ContextCompat.getColor(this, R.color.red)
                                )
                            }

                            result.data.results.forEach {
                                arrayPaket.add(it.nama_paket)
                            }

                            val adapterPaket = ArrayAdapter(
                                applicationContext, android.R.layout.simple_spinner_dropdown_item,
                                arrayPaket
                            )


                            activityBookingBinding.paket.setAdapter(adapterPaket)

                            activityBookingBinding.paket.onItemClickListener =
                                AdapterView.OnItemClickListener { _, _, position, _ ->

                                    paketId = result.data.results[position].id

                                    activityBookingBinding.layoutPaket.isErrorEnabled = false

                                    priceService = result.data.results[position].harga


                                    activityBookingBinding.price.text =
                                        ConvertCurrency().toRupiah(result.data.results[position].harga.toDouble())

                                }

                        }

                        is ResultProcess.Error -> {

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

    private fun loadLayanan() {
        generalViewModel.layanan()
            .observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultProcess.Loading -> {

                        }

                        is ResultProcess.Success -> {
                            val arrayLayanan = ArrayList<String>()

                            result.data.results.forEach {
                                arrayLayanan.add(it.layanan)
                            }

                            val adapterLayanan = ArrayAdapter(
                                applicationContext, android.R.layout.simple_spinner_dropdown_item,
                                arrayLayanan
                            )


                            activityBookingBinding.layanan.setAdapter(adapterLayanan)

                            activityBookingBinding.layanan.onItemClickListener =
                                AdapterView.OnItemClickListener { _, _, position, _ ->

                                    layananId = result.data.results[position].id

                                    //set data paket menjadi empty serta paket_id menjadi null
                                    paketId = null
                                    activityBookingBinding.paket.setText("")

                                    activityBookingBinding.layoutLayanan.isErrorEnabled = false

                                    //jika layanan yang dipilih klinik
                                    if (layananId == 1) {
                                        //load data dokter hewan
                                        activityBookingBinding.layoutDoctor.visibility =
                                            View.VISIBLE
                                        loadDoctorData()
                                    } else {
                                        activityBookingBinding.layoutDoctor.visibility = View.GONE
                                    }

                                    loadPaket(layananId!!)
                                }

                        }

                        is ResultProcess.Error -> {

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


    private fun showLayoutDate(textView: TextView) {
        sheetDialog = BottomSheetDialog(this)
        layoutBottomDateBinding =
            LayoutBottomDateBinding.inflate(
                LayoutInflater.from(this),
                null,
                false
            )
        sheetDialog.setContentView(layoutBottomDateBinding.root)

        showDatePicker(textView)


        sheetDialog.show()
    }

    private fun showDatePicker(textView: TextView) {
        val customDatePicker = CustomDatePickerView(layoutBottomDateBinding.root.context)

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        customDatePicker.setDate(currentYear, currentMonth, currentDay)

        var selectedDate = "$currentDay $currentMonth $currentYear"

        layoutBottomDateBinding.txtDate.text =
            ConvertFormatDate().parseDateFirst(selectedDate)

        customDatePicker.setOnDateChangedListener(object :
            CustomDatePickerView.OnDateChangedListener {
            override fun onDateChanged(year: Int, month: Int, day: Int) {
                // Menangani perubahan tanggal yang dipilih

                selectedDate = "$day $month $year"
                layoutBottomDateBinding.txtDate.text =
                    ConvertFormatDate().parseDateFirst(selectedDate)
            }
        })

        // Tambahkan custom view pada layout yang diinginkan
        val layout = layoutBottomDateBinding.root.findViewById<LinearLayout>(R.id.my_layout)
        layout.addView(customDatePicker)

        layoutBottomDateBinding.buttonConfirmDate.setOnClickListener {

            textView.text = ConvertFormatDate().parseDateFirst(selectedDate)

            activityBookingBinding.layoutBookingDate.isErrorEnabled = false
            sheetDialog.dismiss()
        }

    }

    private fun showTimePickerDialog(timePicker: AppCompatEditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { view, hourOfDay, minute ->
                // Update TextView dengan waktu yang dipilih
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)

                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val formattedTime = timeFormat.format(selectedTime.time)

                timePicker.setText(formattedTime)
            },
            hour,
            minute,
            DateFormat.is24HourFormat(this)
        )

        timePickerDialog.show()
    }

    private fun loadAnimalUser() {
        animalViewModel.animal(loginResponse.token, loginResponse.dataUser.pelanggan!!.id)
            .observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultProcess.Loading -> {

                        }

                        is ResultProcess.Success -> {
                            val arrayAnimal = ArrayList<String>()

                            result.data.results.forEach {
                                arrayAnimal.add(it.namaHewan)
                            }

                            val adapterProperty = ArrayAdapter(
                                applicationContext, android.R.layout.simple_spinner_dropdown_item,
                                arrayAnimal
                            )


                            activityBookingBinding.animal.setAdapter(adapterProperty)

                            activityBookingBinding.animal.onItemClickListener =
                                AdapterView.OnItemClickListener { _, _, position, _ ->

                                    animalId = result.data.results[position].id
                                    activityBookingBinding.layoutAnimal.isErrorEnabled = false
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

    private fun loadDoctorData() {

        doctorViewModel.doctor(loginResponse.token).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultProcess.Loading -> {
                        loadingDialog = ShowLoadingDialog()
                        loadingDialog.startDialog(getString(R.string.please_wait), this)
                    }

                    is ResultProcess.Success -> {
                        loadingDialog.dismissDialog()

                        val arrayDoctor = ArrayList<String>()

                        result.data.results.forEach {
                            arrayDoctor.add(it.namaDokter.toString())
                        }

                        val adapterDoctor = ArrayAdapter(
                            applicationContext, android.R.layout.simple_spinner_dropdown_item,
                            arrayDoctor
                        )


                        activityBookingBinding.doctor.setAdapter(adapterDoctor)

                        activityBookingBinding.doctor.onItemClickListener =
                            AdapterView.OnItemClickListener { _, _, position, _ ->

                                doctorId = result.data.results[position].id

                                activityBookingBinding.layoutDoctor.isErrorEnabled = false
                            }
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

    companion object {
        private var animalId: Int? = null
        private var layananId: Int? = null
        private var paketId: Int? = null
        private var doctorId: Int? = null
        private var loadingDialog = ShowLoadingDialog()
        private var priceService: String = ""
    }
}