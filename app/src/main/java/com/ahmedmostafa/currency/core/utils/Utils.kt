package com.ahmedmostafa.currency.core.utils

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