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
import androidx.recyclerview.widget.LinearLayoutManager
import aqilla.com.pitpitpetcare.R
import aqilla.com.pitpitpetcare.adapter.AdapterAnimal
import aqilla.com.pitpitpetcare.data.source.repository.ResultProcess
import aqilla.com.pitpitpetcare.data.source.response.LoginResponse
import aqilla.com.pitpitpetcare.databinding.FragmentHewanBinding
import aqilla.com.pitpitpetcare.ui.animal.ManageAnimalActivity
import aqilla.com.pitpitpetcare.ui.auth.LoginActivity
import aqilla.com.pitpitpetcare.ui.booking.BookingActivity
import aqilla.com.pitpitpetcare.ui.viewmodel.AnimalViewModel
import aqilla.com.pitpitpetcare.utils.MySharedPreferences
import aqilla.com.pitpitpetcare.utils.MyToast
import aqilla.com.pitpitpetcare.viewmodel.ViewModelFactoryAnimal

class HewanFragment : Fragment() {
    private lateinit var binding: FragmentHewanBinding

    private lateinit var loginResponse: LoginResponse
    private lateinit var animalViewModel: AnimalViewModel

    private lateinit var mySharedPreferences: MySharedPreferences

    var mActivity: Activity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHewanBinding.inflate(inflater, container, false)

        val factoryAnimal = ViewModelFactoryAnimal.getInstance(requireActivity().application)

        // Initialize ViewModel using the factory
        animalViewModel = ViewModelProvider(this, factoryAnimal)[AnimalViewModel::class.java]


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Mengubah judul Action Bar saat fragment ditampilkan kembali dari tumpukan (back stack)
        activity?.title = activity?.resources?.getString(R.string.hewan)

        // Mengatur warna ikon FAB
        val colorStateList = ContextCompat.getColorStateList(requireContext(), R.color.white)
        binding.addAnimal.imageTintList = colorStateList

        mySharedPreferences = MySharedPreferences(requireActivity())

        loginResponse = mySharedPreferences.getAuthData()

        binding.rvAnimal.layoutManager = LinearLayoutManager(requireContext())



        binding.addAnimal.setOnClickListener {
            val intent = Intent(activity, ManageAnimalActivity::class.java)
            activity?.startActivity(intent)
        }
    }

    private fun loadAnimalUser() {
        animalViewModel.animal(loginResponse.token, loginResponse.dataUser.pelanggan!!.id)
            .observe(requireActivity()) { result ->
                if (result != null) {
                    when (result) {
                        is ResultProcess.Loading -> {
                            binding.loading.visibility = View.VISIBLE
                        }

                        is ResultProcess.Success -> {
                            binding.loading.visibility = View.GONE

                            if (result.data.results.isEmpty()) {
                                binding.emptyData.visibility = View.VISIBLE
                            }else{
                                binding.emptyData.visibility = View.GONE
                            }
                            val adapterAnimal = AdapterAnimal(result.data.results)
                            adapterAnimal.notifyDataSetChanged()

                            binding.rvAnimal.adapter = adapterAnimal

                        }

                        is ResultProcess.Error -> {
                            binding.loading.visibility = View.VISIBLE
                            if(result.error=="401"){
                                mySharedPreferences.logout()

                                val myToast = MyToast()
                                myToast.showToast(
                                    mActivity!!,
                                    "Sesi telah habis, Silahkan login kembali",
                                    ContextCompat.getColor(mActivity!!, R.color.red)
                                )

                                Intent(mActivity, LoginActivity::class.java).also {
                                    it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(it)
                                    mActivity!!.finish()
                                }
                            }else{
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

    override fun onResume() {
        loadAnimalUser()
        super.onResume()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mActivity = if (context is Activity) {
            context
        } else {
            null
        }
    }
}