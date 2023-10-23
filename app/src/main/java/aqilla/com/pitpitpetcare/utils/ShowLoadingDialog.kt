package aqilla.com.pitpitpetcare.utils

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import aqilla.com.pitpitpetcare.databinding.ItemLoadingBinding

class ShowLoadingDialog {

    fun startDialog(message: String, activity: Activity) {
        val builder: androidx.appcompat.app.AlertDialog.Builder =
            androidx.appcompat.app.AlertDialog.Builder(activity)
        val itemMenuBinding =
            ItemLoadingBinding.inflate(LayoutInflater.from(activity), null, false)
        builder.setView(itemMenuBinding.root)

        itemMenuBinding.messageLoading.text = message

        alertDialog = builder.create()
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog?.setCancelable(false)
        alertDialog?.show()
    }

    companion object {
        var alertDialog: androidx.appcompat.app.AlertDialog? = null
    }

    fun dismissDialog() {
        alertDialog?.dismiss()
    }
}