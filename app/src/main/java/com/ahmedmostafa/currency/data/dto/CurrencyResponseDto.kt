package com.ahmedmostafa.currency.data.dto

import com.google.gson.annotations.SerializedName

data class CurrencyResponseDto(
    @SerializedName("symbols") val symbols: Map<String, String>,
    @SerializedName("error") val error: ApiError?,
    @SerializedName("success") val success: Boolean,

    )