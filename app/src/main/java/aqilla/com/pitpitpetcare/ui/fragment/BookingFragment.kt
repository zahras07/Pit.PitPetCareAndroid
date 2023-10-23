package aqilla.com.pitpitpetcare.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import aqilla.com.pitpitpetcare.R
import aqilla.com.pitpitpetcare.adapter.AdapterBooking
import aqilla.com.pitpitpetcare.data.source.repository.ResultProcess
import aqilla.com.pitpitpetcare.data.source.response.LoginResponse
import aqilla.com.pitpitpetcare.databinding.FragmentBookingBinding
import aqilla.com.pitpitpetcare.ui.auth.LoginActivity
import aqilla.com.pitpitpetcare.ui.booking.BookingActivity
import aqilla.com.pitpitpetcare.ui.viewmodel.BookingViewModel
import aqilla.com.pitpitpetcare.utils.MySharedPreferences
import aqilla.com.pitpitpetcare.utils.MyToast
import aqilla.com.pitpitpetcare.utils.ShowLoadingDialog
import aqilla.com.pitpitpetcare.viewmodel.ViewModelFactoryBooking


class BookingFragment : Fragment(), AdapterBooking.SetOnClickBooking {

    private lateinit var fragmentBookingBinding: FragmentBookingBinding

    private lateinit var loginResponse: LoginResponse
    private lateinit var bookingViewModel: BookingViewModel
    private lateinit var mySharedPreferences: MySharedPreferences
    var mActivity: Activity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        fragmentBookingBinding = FragmentBookingBinding.inflate(inflater, container, false)

        val factoryBooking =
            ViewModelFactoryBooking.getInstance(requireActivity().applicationContext)

        // Initialize ViewModel using the factory
        bookingViewModel = ViewModelProvider(this, factoryBooking)[BookingViewModel::class.java]


        return fragmentBookingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Mengubah judul Action Bar saat fragment ditampilkan kembali dari tumpukan (back stack)
        activity?.title = activity?.resources?.getString(R.string.booking)


        fragmentBookingBinding.addBooking.setOnClickListener {
            val intent = Intent(activity, BookingActivity::class.java)
            activity?.startActivity(intent)
        }


        mySharedPreferences = MySharedPreferences(requireActivity())

        loginResponse = mySharedPreferences.getAuthData()

        fragmentBookingBinding.rvBooking.layoutManager = LinearLayoutManager(requireContext())

    }


    private fun loadBooking() {
        bookingViewModel.booking(loginResponse.token).observe(requireActivity()) { result ->
            if (result != null) {
                when (result) {
                    is ResultProcess.Loading -> {
                        fragmentBookingBinding.loading.visibility = View.VISIBLE
                    }

                    is ResultProcess.Success -> {
                        fragmentBookingBinding.loading.visibility = View.GONE

                        if (result.data.data.isEmpty()) {
                            fragmentBookingBinding.emptyData.visibility = View.VISIBLE
                        }else{
                            fragmentBookingBinding.emptyData.visibility = View.GONE
                        }
                        val adapterBooking =
                            AdapterBooking(result.data.data, loginResponse.dataUser, this)
                        adapterBooking.notifyDataSetChanged()

                        fragmentBookingBinding.rvBooking.adapter = adapterBooking

                    }

                    is ResultProcess.Error -> {
                        fragmentBookingBinding.loading.visibility = View.VISIBLE
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

    override fun onClickCancelBooking(bookingId: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(resources.getString(R.string.booking))
        builder.setMessage("Yakin ingin mebatalkan Booking")
        builder.setPositiveButton("Ya") { _, _ ->

            bookingViewModel.cancelBooking(loginResponse.token, bookingId)
                .observe(requireActivity()) { result ->
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

                                loadBooking()
                            }

                            is ResultProcess.Error -> {
                                loadingDialog.dismissDialog()
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

        builder.setNegativeButton("Batal") { dialog, _ ->

            dialog.dismiss()

        }

        builder.show()
    }

    override fun onResume() {
        super.onResume()

        loadBooking()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mActivity = if (context is Activity) {
            context
        } else {
            null
        }
    }

    companion object {

        private var loadingDialog = ShowLoadingDialog()
    }

}