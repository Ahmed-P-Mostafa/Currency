package com.ahmedmostafa.currency.data.dto


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

    @Parcelize
    data class ApiError(
        @SerializedName("code") val code: Int,
        @SerializedName("info") val info: String,
        @SerializedName("type") val type: String?
    ) : Parcelable
