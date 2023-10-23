package aqilla.com.pitpitpetcare.utils

import android.app.Activity
import android.content.Context
import aqilla.com.pitpitpetcare.data.source.response.LoginResponse
import com.google.gson.Gson

class MySharedPreferences(private val activity: Activity) {

    fun saveData(
        data: LoginResponse,
        gson: Gson,
        nameSharedPreferences: String,
        valuesKey: String
    ) {

        val jsonString = gson.toJson(data)
        val sharedPreferences =
            activity.getSharedPreferences(nameSharedPreferences, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(valuesKey, jsonString)
        editor.apply()

    }

    fun getAuthData(): LoginResponse {
        val sharedPreferences = activity.getSharedPreferences("auth", Context.MODE_PRIVATE)

        return Gson().fromJson(sharedPreferences.getString("data", ""), LoginResponse::class.java)
    }

    fun logout() {
        val sharedPreferences = activity.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("data")
        editor.clear()
        editor.apply()

    }
}