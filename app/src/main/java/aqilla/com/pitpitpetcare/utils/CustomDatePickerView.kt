package aqilla.com.pitpitpetcare.utils

import android.content.Context
import android.view.LayoutInflater
import android.widget.NumberPicker
import androidx.appcompat.widget.LinearLayoutCompat
import aqilla.com.pitpitpetcare.R
import java.util.Calendar

class CustomDatePickerView(context: Context) :
    LinearLayoutCompat(context) {
    private val dayPicker: NumberPicker
    private val monthPicker: NumberPicker
    private val yearPicker: NumberPicker

    private var onDateChangedListener: OnDateChangedListener? = null

    interface OnDateChangedListener {
        fun onDateChanged(day: Int, year: Int, month: Int)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_date_custom, this, true)

        // Inisialisasi NumberPicker untuk hari, bulan dan tahun

        dayPicker = findViewById(R.id.day_picker)
        dayPicker.minValue = 1
        dayPicker.maxValue = 31

        monthPicker = findViewById(R.id.month_picker)
        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.displayedValues = arrayOf(
            "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"
        )

        yearPicker = findViewById(R.id.year_picker)
        yearPicker.minValue = 1900
        yearPicker.maxValue = Calendar.getInstance().get(Calendar.YEAR)
        yearPicker.value = Calendar.getInstance().get(Calendar.YEAR)

        // Atur listener untuk kedua NumberPicker
        val onValueChangeListener = NumberPicker.OnValueChangeListener { _, _, _ ->
            val selectedYear = yearPicker.value
            val selectedMonth = monthPicker.value
            val daysInSelectedMonth = getDaysInMonth(selectedYear, selectedMonth)

            dayPicker.maxValue = daysInSelectedMonth

            val selectedDay = dayPicker.value
            if (selectedDay > daysInSelectedMonth) {
                dayPicker.value = daysInSelectedMonth
            }

            onDateChangedListener?.onDateChanged(selectedYear, selectedMonth, dayPicker.value)
        }
        dayPicker.setOnValueChangedListener(onValueChangeListener)
        monthPicker.setOnValueChangedListener(onValueChangeListener)
        yearPicker.setOnValueChangedListener(onValueChangeListener)
    }

    fun setOnDateChangedListener(listener: OnDateChangedListener) {
        onDateChangedListener = listener
    }

    fun setDate(year: Int, month: Int, day: Int) {
        yearPicker.value = year
        monthPicker.value = month
        dayPicker.value = day
    }

    private fun getDaysInMonth(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1)
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
}
