package aqilla.com.pitpitpetcare.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import aqilla.com.pitpitpetcare.R
import aqilla.com.pitpitpetcare.data.source.repository.ResultProcess
import aqilla.com.pitpitpetcare.databinding.ActivityRegisterBinding
import aqilla.com.pitpitpetcare.ui.viewmodel.UserViewModel
import aqilla.com.pitpitpetcare.utils.MyToast
import aqilla.com.pitpitpetcare.utils.ShowLoadingDialog
import aqilla.com.pitpitpetcare.viewmodel.ViewModelFactoryUser

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val viewModelFactoryUser = ViewModelFactoryUser.getInstance(this)
    private val userViewModel: UserViewModel by viewModels { viewModelFactoryUser }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.textToLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {

            if (binding.etNama.text.toString().isEmpty()) {
                Toast.makeText(this, "Nama Lengkap Wajib diisi", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (binding.etEmail.text.toString().isEmpty()) {
                Toast.makeText(this, "Email Wajib diisi", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (binding.etPassword.text.toString().isEmpty()) {
                Toast.makeText(this, "Kata Sandi Wajib diisi", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (binding.etPassword.text.toString().length < 4) {
                Toast.makeText(this, "Panjang kata sandi minimal 4 karakter", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            userViewModel.register(
                binding.etNama.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
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

                            Intent(this, LoginActivity::class.java).also {
                                startActivity(it)
                                finish()
                            }
                        }

                        is ResultProcess.Error -> {
                            loadingDialog.dismissDialog()
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
    }

    companion object {
        private var loadingDialog = ShowLoadingDialog()
    }
}