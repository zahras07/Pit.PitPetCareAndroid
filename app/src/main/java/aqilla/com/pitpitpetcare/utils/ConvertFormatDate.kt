package com.explorindo.tokonedwiplseller.utils

import java.text.SimpleDateFormat
import java.time.Period
import java.time.YearMonth
import java.util.*

class ConvertFormatDate {

    fun parseDateFirst(data: String): String? {

        val inputFormat = SimpleDateFormat("dd M yyyy", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)

        val date = inputFormat.parse(data)
        return date?.let { outputFormat.format(it) }
    }

    fun parseDateTwo(data: String): String? {

        val inputFormat = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("yyyy-M", Locale.ENGLISH)

        val date = inputFormat.parse(data)
        return date?.let { outputFormat.format(it) }
    }

    fun getMonth(data: String): String? {

        val inputFormat = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("M", Locale.ENGLISH)

        val date = inputFormat.parse(data)
        return date?.let { outputFormat.format(it) }
    }

    fun parseDateThree(data: String): String? {

        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)

        val date = inputFormat.parse(data)
        return date?.let { outputFormat.format(it) }
    }

    fun getYear(data: String): String? {

        val inputFormat = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("yyyy", Locale.ENGLISH)

        val date = inputFormat.parse(data)
        return date?.let { outputFormat.format(it) }
    }

    fun calculateNumberMonthTwDates(
        startMonth: Int,
        startYear: Int,
        endMonth: Int,
        endYear: Int
    ): Int {
        val startCal = Calendar.getInstance()
        startCal.set(Calendar.YEAR, startYear)
        startCal.set(Calendar.MONTH, startMonth)
        startCal.set(Calendar.DAY_OF_MONTH, 1)

        val endCal = Calendar.getInstance()
        endCal.set(Calendar.YEAR, endYear)
        endCal.set(Calendar.MONTH, endMonth)
        endCal.set(Calendar.DAY_OF_MONTH, 1)

        var numMonths = 0
        while (startCal.before(endCal)) {
            startCal.add(Calendar.MONTH, 1)
            numMonths++
        }

        return numMonths
    }

    fun currentYear(): String{

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)

        return year.toString()
    }
}