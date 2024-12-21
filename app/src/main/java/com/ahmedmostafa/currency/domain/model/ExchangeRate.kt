package com.ahmedmostafa.currency.domain.model

data class ExchangeRate(
    val fromCurrency: String,
    val toCurrency: String,
    val rate: Double,
)