package com.ahmedmostafa.currency.data.dto

import com.ahmedmostafa.currency.core.utils.ApiError
import com.ahmedmostafa.currency.core.utils.ApiResponse
import com.google.gson.annotations.SerializedName

data class ExchangeRateResponseDto(
    @SerializedName("base") val base: String,
    @SerializedName("date") val date: String,
    @SerializedName("rates") val rates: Map<String, Double>,
    @SerializedName("success") override val success: Boolean,
    @SerializedName("timestamp") val timestamp: Int,
    @SerializedName("error") override val error: ApiError?,

    ) : ApiResponse