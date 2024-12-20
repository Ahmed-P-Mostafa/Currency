package com.ahmedmostafa.currency.data.dto

data class CurrencyResponseDto(
    val success: Boolean,
    val symbols: Map<String, String>
)