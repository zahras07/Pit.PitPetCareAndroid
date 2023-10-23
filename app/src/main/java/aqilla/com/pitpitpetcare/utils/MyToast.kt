package aqilla.com.pitpitpetcare.utils

import android.app.Activity
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import aqilla.com.pitpitpetcare.databinding.CustomToastLayoutBinding

class MyToast {

    fun showToast(activity: Activity, message: String, color: Int) {
        val customToastLayoutBinding =
            CustomToastLayoutBinding.inflate(LayoutInflater.from(activity), null, false)

        customToastLayoutBinding.layoutToast.setBackgroundColor(color)
        customToastLayoutBinding.toastMessage.text = message

        val toast = Toast(activity)
        toast.duration = Toast.LENGTH_SHORT
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.view = customToastLayoutBinding.root
        toast.show()
    }
}