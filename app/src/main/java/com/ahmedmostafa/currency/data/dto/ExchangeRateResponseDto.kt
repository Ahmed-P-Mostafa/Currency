package com.ahmedmostafa.currency.data.dto
data class ExchangeRateResponseDto(
    val success: Boolean,
    val timestamp: Long,
    val base: String,
    val rates: Map<String, Double>
)