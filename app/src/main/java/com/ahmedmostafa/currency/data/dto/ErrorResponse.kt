package com.ahmedmostafa.currency.data.dto


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ApiError(
    @SerializedName("code") val code: Int,
    @SerializedName("info") val info: String,
    @SerializedName("type") val type: String?
) : Parcelable
