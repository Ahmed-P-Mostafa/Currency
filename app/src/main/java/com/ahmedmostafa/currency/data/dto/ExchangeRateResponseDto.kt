package com.ahmedmostafa.currency.data.dto

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponseDto(
    @SerializedName("base") val base: String,
    @SerializedName("rates") val rates: Map<String, Double>,
    @SerializedName("success") val success: Boolean,
    @SerializedName("timestamp") val timestamp: Int,
    @SerializedName("error") val error: ApiError?,

    )