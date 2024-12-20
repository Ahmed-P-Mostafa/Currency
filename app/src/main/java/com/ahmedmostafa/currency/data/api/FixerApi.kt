package com.ahmedmostafa.currency.data.api
import com.ahmedmostafa.currency.data.dto.CurrencyResponseDto
import com.ahmedmostafa.currency.data.dto.ExchangeRateResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface FixerApi {
    @GET("symbols")
    suspend fun getCurrencies(): CurrencyResponseDto

    @GET("latest")
    suspend fun getLatestRate(
        @Query("base") base: String,
        @Query("symbols") symbols: String
    ): ExchangeRateResponseDto

    @GET("timeseries")
    suspend fun getHistoricalRates(
        @Query("base") base: String,
        @Query("symbols") symbols: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): ExchangeRateResponseDto
}