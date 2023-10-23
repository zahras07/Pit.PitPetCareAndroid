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
import aqilla.com.pitpitpetcare.BuildConfig
import aqilla.com.pitpitpetcare.R
import aqilla.com.pitpitpetcare.data.source.model.User
import aqilla.com.pitpitpetcare.data.source.repository.ResultProcess
import aqilla.com.pitpitpetcare.data.source.response.LoginResponse
import aqilla.com.pitpitpetcare.databinding.FragmentProfilBinding
import aqilla.com.pitpitpetcare.ui.auth.LoginActivity
import aqilla.com.pitpitpetcare.ui.user.ManageUserActivity
import aqilla.com.pitpitpetcare.ui.viewmodel.UserViewModel
import aqilla.com.pitpitpetcare.utils.MySharedPreferences
import aqilla.com.pitpitpetcare.utils.MyToast
import aqilla.com.pitpitpetcare.viewmodel.ViewModelFactoryUser
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlin.math.log


class ProfilFragment : Fragment() {


    private lateinit var profileBinding: FragmentProfilBinding
    private lateinit var mySharedPreferences: MySharedPreferences
    private lateinit var loginResponse: LoginResponse
    var mActivity: Activity? = null

    private lateinit var userViewModel: UserViewModel
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileBinding = FragmentProfilBinding.inflate(inflater, container, false)

        val factoryUser =
            ViewModelFactoryUser.getInstance(requireActivity().applicationContext)

        // Initialize ViewModel using the factory
        userViewModel = ViewModelProvider(this, factoryUser)[UserViewModel::class.java]

        return profileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Mengubah judul Action Bar saat fragment ditampilkan kembali dari tumpukan (back stack)
        activity?.title = activity?.resources?.getString(R.string.profile)

        mySharedPreferences = MySharedPreferences(mActivity!!)

        loginResponse = mySharedPreferences.getAuthData()

        user = loginResponse.dataUser
        setData()


        profileBinding.rlLogout.setOnClickListener {

            val builder: AlertDialog.Builder = AlertDialog.Builder(mActivity!!)
            builder.setTitle(resources.getString(R.string.keluar))
            builder.setMessage(getString(R.string.alert_message_exit_application))
            builder.setPositiveButton("Ok") { _, _ ->


                mySharedPreferences.logout()

                Intent(mActivity!!, LoginActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(it)
                    activity?.finish()
                }
            }

            builder.setNegativeButton("No") { dialog, _ ->

                dialog.dismiss()

            }

            builder.show()
        }

        profileBinding.lihatProfil.setOnClickListener {
            val intent = Intent(activity, ManageUserActivity::class.java)
            intent.putExtra("data", user)
            activity?.startActivity(intent)
        }

        profileBinding.profileImage.setOnClickListener {

        }
    }

    private fun setData() {

        Glide.with(this).load(BuildConfig.BASE_URL_ASSET + user.pelanggan!!.foto)
            .error(R.drawable.default_profile_user).placeholder(R.drawable.default_profile_user)
            .into(profileBinding.profileImage)
        profileBinding.fullName.text = user.name
        profileBinding.email.text = user.email

    }

    private fun loadUser() {


        userViewModel.user(
            loginResponse.token,
        ).observe(requireActivity()) { result ->


            if (result != null) {
                when (result) {
                    is ResultProcess.Loading -> {

                    }

                    is ResultProcess.Success -> {
                        user = result.data.dataUser

                        setData()
                    }

                    is ResultProcess.Error -> {

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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mActivity = if (context is Activity) {
            context
        } else {
            null
        }
    }


    override fun onResume() {
        super.onResume()
        loadUser()
    }
}