package com.explorindo.tokonedwiplseller.utils

import java.text.NumberFormat
import java.util.*

class ConvertCurrency {

    fun toRupiah(harga: Double?): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        val result = numberFormat.format(harga)
        return result.replace(",00", "")
    }
}