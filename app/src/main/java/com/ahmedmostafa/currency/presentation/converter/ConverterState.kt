package com.ahmedmostafa.currency.presentation.converter

import com.ahmedmostafa.currency.domain.model.Currency

data class ConverterState(
    val currencies: Map<String, String> = emptyMap(),
    val rates: Map<String, Double> = emptyMap(),
    val fromCurrency: Currency = Currency(),
    val toCurrency: Currency = Currency(),
    val amount: String = "1.00",
    val convertedAmount: String = "0.00",
    val error: String = "",
    val isLoading: Boolean = false,

    )