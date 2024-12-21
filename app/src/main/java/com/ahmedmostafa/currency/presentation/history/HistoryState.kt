package com.ahmedmostafa.currency.presentation.history

import com.ahmedmostafa.currency.domain.model.HistoricalRate

data class HistoryState(
    val fromCurrency: String = "",
    val toCurrency: String = "",
    val historicalRates: List<HistoricalRate> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)