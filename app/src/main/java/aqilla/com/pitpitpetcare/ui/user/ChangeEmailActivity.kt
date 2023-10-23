package aqilla.com.pitpitpetcare.ui.user

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import aqilla.com.pitpitpetcare.R
import aqilla.com.pitpitpetcare.data.source.repository.ResultProcess
import aqilla.com.pitpitpetcare.data.source.response.LoginResponse
import aqilla.com.pitpitpetcare.databinding.ActivityChangeEmailBinding
import aqilla.com.pitpitpetcare.ui.auth.LoginActivity
import aqilla.com.pitpitpetcare.ui.viewmodel.UserViewModel
import aqilla.com.pitpitpetcare.utils.MySharedPreferences
import aqilla.com.pitpitpetcare.utils.MyToast
import aqilla.com.pitpitpetcare.utils.ShowLoadingDialog
import aqilla.com.pitpitpetcare.viewmodel.ViewModelFactoryUser

class ChangeEmailActivity : AppCompatActivity() {

    private lateinit var changeEmailBinding: ActivityChangeEmailBinding
    private lateinit var loginResponse: LoginResponse

    private lateinit var mySharedPreferences: MySharedPreferences

    private val factoryUser = ViewModelFactoryUser.getInstance(this)
    private val userViewModel: UserViewModel by viewModels { factoryUser }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeEmailBinding = ActivityChangeEmailBinding.inflate(layoutInflater)
        setContentView(changeEmailBinding.root)

        mySharedPreferences = MySharedPreferences(this)

        loginResponse = mySharedPreferences.getAuthData()

        changeEmailBinding.changeEmail.setOnClickListener {
            changeEmail()
        }
    }

    private fun changeEmail() {
        userViewModel.changeEmail(
            loginResponse.token,
            changeEmailBinding.newEmail.text.toString(),
            changeEmailBinding.currentPassword.text.toString(),
        ).observe(this) { result ->


            if (result != null) {
                when (result) {
                    is ResultProcess.Loading -> {
                        loading = ShowLoadingDialog()
                        loading.startDialog(
                            getString(R.string.please_wait),
                            this
                        )
                    }

                    is ResultProcess.Success -> {
                        loading.dismissDialog()

                        val myToast = MyToast()
                        myToast.showToast(
                            this,
                            result.data.message,
                            ContextCompat.getColor(applicationContext, R.color.green)
                        )
                        mySharedPreferences.logout()

                        Intent(this, LoginActivity::class.java).also {
                            it.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(it)
                            finish()
                        }
                    }

                    is ResultProcess.Error -> {
                        loading.dismissDialog()
                        if(result.error=="401"){
                            mySharedPreferences.logout()

                            val myToast = MyToast()
                            myToast.showToast(
                                this,
                                "Sesi telah habis, Silahkan login kembali",
                                ContextCompat.getColor(this, R.color.red)
                            )

                            Intent(this, LoginActivity::class.java).also {
                                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(it)
                                finish()
                            }
                        }else{
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
        private var loading = ShowLoadingDialog()
    }
}