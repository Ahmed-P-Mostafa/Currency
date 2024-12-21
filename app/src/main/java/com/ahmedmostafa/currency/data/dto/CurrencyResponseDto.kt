package com.ahmedmostafa.currency.data.dto

import com.ahmedmostafa.currency.core.utils.ApiError
import com.ahmedmostafa.currency.core.utils.ApiResponse
import com.google.gson.annotations.SerializedName

data class CurrencyResponseDto(
    @SerializedName("symbols") val symbols: Map<String, String>,
    @SerializedName("error") override val error: ApiError?,
    @SerializedName("success") override val success: Boolean,

    ) : ApiResponse