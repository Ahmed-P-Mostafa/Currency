package com.ahmedmostafa.currency.data.api

import com.ahmedmostafa.currency.data.dto.CurrencyResponseDto
import com.ahmedmostafa.currency.data.dto.ExchangeRateResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FixerApi {
    @GET("symbols")
    suspend fun getCurrencies(): CurrencyResponseDto

    @GET("latest")
    suspend fun getLatestRate(
        @Query("base") base: String? = null,
        @Query("symbols") symbols: String? = null
    ): ExchangeRateResponseDto

    @GET("{date}")
    suspend fun getHistoricalRates(
        @Path("date") date: String,
    ): ExchangeRateResponseDto
}