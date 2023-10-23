package aqilla.com.pitpitpetcare.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import aqilla.com.pitpitpetcare.R
import aqilla.com.pitpitpetcare.data.source.repository.ResultProcess
import aqilla.com.pitpitpetcare.databinding.ActivityLoginBinding
import aqilla.com.pitpitpetcare.ui.MainActivity
import aqilla.com.pitpitpetcare.ui.viewmodel.UserViewModel
import aqilla.com.pitpitpetcare.utils.MySharedPreferences
import aqilla.com.pitpitpetcare.utils.ShowLoadingDialog
import aqilla.com.pitpitpetcare.viewmodel.ViewModelFactoryUser
import com.google.gson.Gson

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val factoryUser = ViewModelFactoryUser.getInstance(this)
    private var mySharedPreferences = MySharedPreferences(this)
    private val userViewModel: UserViewModel by viewModels { factoryUser }

    private var gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.elevation = 0f

        checkLogin()


        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            loginUser(email, password)
        }
        binding.textToRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loginUser(email: String, password: String) {


        userViewModel.login(email, password).observe(this) { result ->


            if (result != null) {
                when (result) {
                    is ResultProcess.Loading -> {
                        loading = ShowLoadingDialog()
                        loading.startDialog(getString(R.string.please_wait), this)
                    }

                    is ResultProcess.Success -> {
                        loading.dismissDialog()
                        gson = Gson()
                        mySharedPreferences.saveData(result.data, gson, "auth", "data")

                        Intent(this, MainActivity::class.java).also {
                            it.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(it)
                            finish()
                        }
                    }

                    is ResultProcess.Error -> {
                        loading.dismissDialog()
                        Toast.makeText(
                            this,
                            result.error,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }


    private fun checkLogin() {
        val sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)

        if (sharedPreferences.contains("data")) {
            Intent(this, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(it)
                finish()
            }
        }
    }

    companion object {
        private var loading = ShowLoadingDialog()
    }
}