package com.ahmedmostafa.currency.domain.model

import java.time.LocalDate

data class HistoricalRate(
    val date: LocalDate,
    val rate: ExchangeRate
)