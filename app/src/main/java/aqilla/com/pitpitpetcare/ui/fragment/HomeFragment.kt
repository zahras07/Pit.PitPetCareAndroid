package aqilla.com.pitpitpetcare.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import aqilla.com.pitpitpetcare.R
import aqilla.com.pitpitpetcare.adapter.AdapterDoctor
import aqilla.com.pitpitpetcare.adapter.AdapterPackageService
import aqilla.com.pitpitpetcare.adapter.AdapterSchedule
import aqilla.com.pitpitpetcare.data.source.model.Paket
import aqilla.com.pitpitpetcare.data.source.repository.ResultProcess
import aqilla.com.pitpitpetcare.data.source.response.LoginResponse
import aqilla.com.pitpitpetcare.databinding.BottomsheetPackageServiceBinding
import aqilla.com.pitpitpetcare.databinding.FragmentHomeBinding
import aqilla.com.pitpitpetcare.ui.auth.LoginActivity
import aqilla.com.pitpitpetcare.ui.dokter.DetailDokterActivity
import aqilla.com.pitpitpetcare.ui.viewmodel.DoctorViewModel
import aqilla.com.pitpitpetcare.ui.viewmodel.GeneralViewModel
import aqilla.com.pitpitpetcare.utils.MySharedPreferences
import aqilla.com.pitpitpetcare.utils.MyToast
import aqilla.com.pitpitpetcare.utils.ShowLoadingDialog
import aqilla.com.pitpitpetcare.viewmodel.ViewModelFactoryDoctor
import aqilla.com.pitpitpetcare.viewmodel.ViewModelFactoryGeneral
import com.explorindo.tokonedwiplseller.utils.ConvertCurrency
import com.google.android.material.bottomsheet.BottomSheetDialog

class HomeFragment : Fragment(), AdapterPackageService.SetOnClickPackageService,
    AdapterDoctor.SetOnClickDoctor {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var loginResponse: LoginResponse
    private lateinit var doctorViewModel: DoctorViewModel
    private lateinit var mySharedPreferences: MySharedPreferences

    private lateinit var generalViewModel: GeneralViewModel

    var mActivity: Activity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        val factoryDoctor = ViewModelFactoryDoctor.getInstance(requireActivity().applicationContext)
        val factoryGeneral =
            ViewModelFactoryGeneral.getInstance(requireActivity().applicationContext)

        // Initialize ViewModel using the factory
        doctorViewModel = ViewModelProvider(this, factoryDoctor)[DoctorViewModel::class.java]
        generalViewModel = ViewModelProvider(this, factoryGeneral)[GeneralViewModel::class.java]


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mySharedPreferences = MySharedPreferences(mActivity!!)

        loginResponse = mySharedPreferences.getAuthData()

        binding.rvDokter.layoutManager = LinearLayoutManager(mActivity!!)

        loadPackageService()

        loadSchedule();

    }

    private fun loadSchedule() {
        doctorViewModel.schedule(loginResponse.token).observe(requireActivity()) { result ->
            if (result != null) {
                when (result) {
                    is ResultProcess.Loading -> {
                        binding.loadSchedule.visibility = View.VISIBLE
                    }

                    is ResultProcess.Success -> {
                        binding.loadSchedule.visibility = View.GONE

                        if (result.data.data.isEmpty()) {
                            binding.emptySchedule.visibility = View.VISIBLE
                        }

                        binding.rvSchedule.layoutManager = GridLayoutManager(
                            requireContext(),
                            1,
                            GridLayoutManager.HORIZONTAL,
                            false
                        )


                        val adapterSchedule = AdapterSchedule(result.data.data)
                        adapterSchedule.notifyDataSetChanged()

                        binding.rvSchedule.adapter = adapterSchedule
                        loadDoctorData()
                    }

                    is ResultProcess.Error -> {
                        binding.loadSchedule.visibility = View.VISIBLE
                        if (result.error == "401") {
                            mySharedPreferences.logout()

                            val myToast = MyToast()
                            myToast.showToast(
                                mActivity!!,
                                "Sesi telah habis, Silahkan login kembali",
                                ContextCompat.getColor(mActivity!!, R.color.red)
                            )

                            Intent(mActivity!!, LoginActivity::class.java).also {
                                it.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(it)
                                mActivity!!.finish()
                            }
                        } else {
                            val myToast = MyToast()
                            myToast.showToast(
                                mActivity!!,
                                result.error,
                                ContextCompat.getColor(mActivity!!, R.color.red)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadPackageService() {
        generalViewModel.allPackage().observe(requireActivity()) { result ->
            if (result != null) {
                when (result) {
                    is ResultProcess.Loading -> {
                        binding.loadingPackageService.visibility = View.VISIBLE
                    }

                    is ResultProcess.Success -> {
                        binding.loadingPackageService.visibility = View.GONE

                        if (result.data.results.isEmpty()) {
                            binding.emptyDataPackageService.visibility = View.VISIBLE
                        }

                        val totalItems =
                            result.data.results.size // Jumlah total item dalam dataset Anda
                        val columnCount = if (totalItems <= 5) {
                            1
                        } else {
                            2
                        }
                        binding.rvPackageService.layoutManager = GridLayoutManager(
                            requireContext(),
                            columnCount,
                            GridLayoutManager.HORIZONTAL,
                            false
                        )


                        val adapterPackageService = AdapterPackageService(result.data.results, this)
                        adapterPackageService.notifyDataSetChanged()

                        binding.rvPackageService.adapter = adapterPackageService

                    }

                    is ResultProcess.Error -> {
                        binding.loadingPackageService.visibility = View.VISIBLE
                        val myToast = MyToast()
                        myToast.showToast(
                            requireActivity(),
                            result.error,
                            ContextCompat.getColor(requireContext(), R.color.red)
                        )
                    }
                }
            }
        }
    }

    private fun loadDoctorData() {

        doctorViewModel.doctor(loginResponse.token).observe(requireActivity()) { result ->
            if (result != null) {
                when (result) {
                    is ResultProcess.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                    }

                    is ResultProcess.Success -> {
                        binding.loading.visibility = View.GONE

                        if (result.data.results.isEmpty()) {
                            binding.emptyData.visibility = View.VISIBLE
                        }
                        val adapterDoctor =
                            AdapterDoctor(result.data.results, requireActivity(), this)
                        adapterDoctor.notifyDataSetChanged()

                        binding.rvDokter.adapter = adapterDoctor

                    }

                    is ResultProcess.Error -> {
                        binding.loading.visibility = View.VISIBLE
                        if (result.error == "401") {
                            mySharedPreferences.logout()

                            val myToast = MyToast()
                            myToast.showToast(
                                mActivity!!,
                                "Sesi telah habis, Silahkan login kembali",
                                ContextCompat.getColor(mActivity!!, R.color.red)
                            )

                            Intent(mActivity!!, LoginActivity::class.java).also {
                                it.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(it)
                                mActivity!!.finish()
                            }
                        } else {
                            val myToast = MyToast()
                            myToast.showToast(
                                mActivity!!,
                                result.error,
                                ContextCompat.getColor(mActivity!!, R.color.red)
                            )
                        }

                    }
                }
            }
        }
    }

    override fun onClickPackageService(packageService: Paket) {
        // Inflate tampilan menggunakan ViewBinding
        val bottomSheetBinding = BottomsheetPackageServiceBinding.inflate(layoutInflater)
        val view = bottomSheetBinding.root

        // Buat dan konfigurasi BottomSheetDialog
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle)
        bottomSheetDialog.setContentView(view)
        bottomSheetBinding.packageServiceName.text = packageService.nama_paket
        bottomSheetBinding.descriptionPackageService.text = packageService.deskripsi
        bottomSheetBinding.price.text = ConvertCurrency().toRupiah(packageService.harga.toDouble())

        // Tampilkan BottomSheetDialog
        bottomSheetDialog.show()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mActivity = if (context is Activity) {
            context
        } else {
            null
        }
    }

    override fun onClickDoctor(doctorId: Int) {
        //loadDoctorDetail
        doctorViewModel.doctorDetail(loginResponse.token, doctorId).observe(requireActivity()) { result ->
            if (result != null) {
                when (result) {
                    is ResultProcess.Loading -> {
                        loadingDialog = ShowLoadingDialog()
                        loadingDialog.startDialog(
                            getString(R.string.please_wait),
                            requireActivity()
                        )
                    }

                    is ResultProcess.Success -> {
                        loadingDialog.dismissDialog()
                        Intent(activity, DetailDokterActivity::class.java).also {
                            it.putExtra("data", result.data.data)
                            startActivity(it)
                        }
                    }

                    is ResultProcess.Error -> {
                        loadingDialog.dismissDialog()
                        if (result.error == "401") {
                            mySharedPreferences.logout()

                            val myToast = MyToast()
                            myToast.showToast(
                                mActivity!!,
                                "Sesi telah habis, Silahkan login kembali",
                                ContextCompat.getColor(mActivity!!, R.color.red)
                            )

                            Intent(mActivity!!, LoginActivity::class.java).also {
                                it.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(it)
                                mActivity!!.finish()
                            }
                        } else {
                            val myToast = MyToast()
                            myToast.showToast(
                                mActivity!!,
                                result.error,
                                ContextCompat.getColor(mActivity!!, R.color.red)
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        private var loadingDialog = ShowLoadingDialog()
    }
}