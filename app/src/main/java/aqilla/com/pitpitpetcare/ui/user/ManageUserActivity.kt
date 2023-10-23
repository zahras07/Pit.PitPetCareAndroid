package aqilla.com.pitpitpetcare.ui.user

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import aqilla.com.pitpitpetcare.BuildConfig
import aqilla.com.pitpitpetcare.R
import aqilla.com.pitpitpetcare.data.source.model.Doctor
import aqilla.com.pitpitpetcare.data.source.model.User
import aqilla.com.pitpitpetcare.data.source.repository.ResultProcess
import aqilla.com.pitpitpetcare.data.source.response.LoginResponse
import aqilla.com.pitpitpetcare.databinding.ActivityManageUserBinding
import aqilla.com.pitpitpetcare.databinding.BottomsheetFileFetchSelectionBinding
import aqilla.com.pitpitpetcare.ui.auth.LoginActivity
import aqilla.com.pitpitpetcare.ui.viewmodel.UserViewModel
import aqilla.com.pitpitpetcare.utils.MySharedPreferences
import aqilla.com.pitpitpetcare.utils.MyToast
import aqilla.com.pitpitpetcare.utils.ShowLoadingDialog
import aqilla.com.pitpitpetcare.viewmodel.ViewModelFactoryUser
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ManageUserActivity : AppCompatActivity() {

    private lateinit var manageUserBinding: ActivityManageUserBinding
    private lateinit var loginResponse: LoginResponse
    private lateinit var user: User
    private lateinit var mySharedPreferences: MySharedPreferences

    private val factoryUser = ViewModelFactoryUser.getInstance(this)
    private val userViewModel: UserViewModel by viewModels { factoryUser }

    //untuk meminta izin camera ke pengguna, jika diberikan izin maka akan membuka aplikasi camera
    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Izin kamera diberikan, Anda dapat mulai menggunakan kamera
                // Membuka Camera
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraLauncher.launch(intent)
            }
        }

    //untuk meminta izin storage ke pengguna, jika diberikan izin maka akan membuka aplikasi storage
    private val requestStoragePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Izin penyimpanan eksternal diberikan, Anda dapat mengakses penyimpanan eksternal
                // Membuka Galeri
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                galleryLauncher.launch(intent)
            }
        }

    //untuk membuka galery di perangkat android
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val selectedImageUri = data.data

                    val projection = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = contentResolver.query(
                        selectedImageUri!!,
                        projection,
                        null,
                        null,
                        null
                    )

                    if (cursor != null) {
                        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                        cursor.moveToFirst()
                        val imagePath = cursor.getString(columnIndex)
                        cursor.close()

                        // Path gambar yang ingin Anda upload
                        val imageFile = File(imagePath)


                        if (loginResponse.dataUser.pelanggan != null) {
                            changePhotoProfileCustomer(imageFile)
                        }


                        // Sekarang, imagePath berisi path file gambar yang dipilih
                        // Anda dapat menggunakannya sesuai kebutuhan Anda
                        Log.d("Image Path", imagePath)
                    } else {
                        // Handle jika cursor null
                    }


                    val bitmap = MediaStore.Images.Media.getBitmap(
                        contentResolver,
                        selectedImageUri
                    )
                    manageUserBinding.profileImage.setImageBitmap(bitmap)
                }
            }
        }

    //untuk membuka camera di perangkat android
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val photo = data.extras?.get("data") as Bitmap
                    val imageFile = convertBitmapToFile(photo)
                    if (loginResponse.dataUser.pelanggan != null) {
                        changePhotoProfileCustomer(imageFile)
                    }
                    manageUserBinding.profileImage.setImageBitmap(photo)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manageUserBinding = ActivityManageUserBinding.inflate(layoutInflater)
        setContentView(manageUserBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.profile)

        mySharedPreferences = MySharedPreferences(this)

        loginResponse = mySharedPreferences.getAuthData()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) (run {
            user = intent.getParcelableExtra("data", User::class.java)!!
        }) else {
            user = intent.getParcelableExtra<User>("data") as User
        }
        manageUserBinding.layoutCustomerData.visibility = View.VISIBLE
        manageUserBinding.customerName.setText(user.pelanggan!!.nama_pelanggan)
        manageUserBinding.customerAddress.setText(user.pelanggan!!.alamat)
        manageUserBinding.phoneNumber.setText(user.pelanggan!!.telepon)
        manageUserBinding.email.text = user.email

        Glide.with(this).load(BuildConfig.BASE_URL_ASSET + user.pelanggan!!.foto)
            .into(manageUserBinding.profileImage)

        manageUserBinding.save.setOnClickListener {
            if (user.pelanggan != null) {
                updateCustomerData()
            }
        }

        manageUserBinding.changePassword.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        manageUserBinding.changeEmail.setOnClickListener {
            val intent = Intent(this, ChangeEmailActivity::class.java)
            startActivity(intent)
        }

        manageUserBinding.changePhotoProfile.setOnClickListener {
            // Inflate tampilan menggunakan ViewBinding
            val bottomsheetFileFetchSelectionBinding =
                BottomsheetFileFetchSelectionBinding.inflate(layoutInflater)

            // Buat dan konfigurasi BottomSheetDialog
            val bottomSheetDialog =
                BottomSheetDialog(this, R.style.BottomSheetDialogStyle)
            bottomSheetDialog.setContentView(bottomsheetFileFetchSelectionBinding.root)

            bottomsheetFileFetchSelectionBinding.layoutCamera.setOnClickListener {

                bottomSheetDialog.dismiss()

                // Cek izin kamera
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // Izin kamera belum diberikan, minta izin
                    requestCameraPermission()
                } else {
                    // Membuka Camera
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    cameraLauncher.launch(intent)
                }
            }

            bottomsheetFileFetchSelectionBinding.layoutGallery.setOnClickListener {
                bottomSheetDialog.dismiss()
                // Cek izin penyimpanan eksternal

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // Cek apakah permission sudah diberikan
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                        // Minta permission
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 100)
                    } else {
                        // Permission sudah diberikan
                        val intent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        galleryLauncher.launch(intent)
                    }
                } else {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // Izin penyimpanan eksternal belum diberikan, minta izin
                        requestStoragePermission()
                    } else {
                        // Membuka Galeri
                        val intent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        galleryLauncher.launch(intent)
                    }
                }
            }

            // Tampilkan BottomSheetDialog
            bottomSheetDialog.show()

        }
    }

    private fun updateCustomerData() {
        userViewModel.updateDataCustomer(
            loginResponse.token,
            user.pelanggan!!.id,
            manageUserBinding.customerName.text.toString(),
            manageUserBinding.customerAddress.text.toString(),
            manageUserBinding.phoneNumber.text.toString()
        ).observe(this) { result ->


            if (result != null) {
                when (result) {
                    is ResultProcess.Loading -> {
                        loading = ShowLoadingDialog()
                        loading.startDialog(getString(R.string.please_wait), this)
                    }

                    is ResultProcess.Success -> {
                        loading.dismissDialog()

                        val myToast = MyToast()
                        myToast.showToast(
                            this,
                            result.data.message,
                            ContextCompat.getColor(applicationContext, R.color.green)
                        )
                    }

                    is ResultProcess.Error -> {
                        loading.dismissDialog()
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

    // Fungsi untuk meminta izin kamera
    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            )
        ) {
            // Tampilkan penjelasan mengapa izin diperlukan (opsional)
        }
        //penggunaan dari requestCameraPermissionLauncher yang telah dideklrasikan sebelumnya
        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    // Fungsi untuk meminta izin penyimpanan eksternal
    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            // Tampilkan penjelasan mengapa izin diperlukan (opsional)
        }
        //penggunaan dari requestStoragePermissionLauncher yang telah dideklrasikan sebelumnya
        requestStoragePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun changePhotoProfileCustomer(imageFile: File) {


        // Konversi file gambar ke RequestBody
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imageFile)
        val imagePart = MultipartBody.Part.createFormData("foto", imageFile.name, requestFile)



        userViewModel.changePhotoProfileCustomer(
            loginResponse.token, user.pelanggan!!.id, imagePart
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
                        val myToast = MyToast()

                        loading.dismissDialog()
                        myToast.showToast(
                            this,
                            result.data.message,
                            ContextCompat.getColor(this, R.color.green)
                        )
                    }

                    is ResultProcess.Error -> {
                        loading.dismissDialog()
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

    private fun convertBitmapToFile(bitmap: Bitmap): File {
        // Buat file sementara di direktori cache aplikasi
        val context = applicationContext
        val filesDir = context.cacheDir
        val imageFile = File(filesDir, "temp_image.jpg")

        // Tulis bitmap ke file
        try {
            val stream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return imageFile
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

    companion object {
        private var loading = ShowLoadingDialog()
    }
}