package com.ahmedmostafa.currency.domain.model

data class HistoricalRate(
    val date: String,
    val exchangeRate: ExchangeRate
)