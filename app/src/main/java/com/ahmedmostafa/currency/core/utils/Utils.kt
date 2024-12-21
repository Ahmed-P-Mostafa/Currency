package com.ahmedmostafa.currency.core.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar

object Utils {
    fun calculateConversionRate(
        fromCurrency: String,
        toCurrency: String,
        rates: Map<String, Double>
    ): Double {
        val fromRate = rates[fromCurrency] ?: throw IllegalArgumentException("Invalid fromCurrency")
        val toRate = rates[toCurrency] ?: throw IllegalArgumentException("Invalid toCurrency")
        return toRate / fromRate
    }
}

@SuppressLint("SimpleDateFormat")
fun getHistoryDaysDates(): List<String> {
    val cal = Calendar.getInstance()
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    val formattedDates = mutableListOf<String>()
    for (i in 0..3) {
        cal.add(Calendar.DAY_OF_YEAR, -1)
        formattedDates.add(sdf.format(cal.time))
    }

    return formattedDates.toList()
}
