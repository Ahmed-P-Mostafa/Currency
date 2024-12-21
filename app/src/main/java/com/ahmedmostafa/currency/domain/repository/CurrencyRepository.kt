package com.ahmedmostafa.currency.domain.repository

import com.ahmedmostafa.currency.core.utils.Resource
import com.ahmedmostafa.currency.data.dto.ExchangeRateResponseDto
import com.ahmedmostafa.currency.domain.model.Currency
import com.ahmedmostafa.currency.domain.model.HistoricalRate

interface CurrencyRepository {
    suspend fun getCurrencies(): Resource<List<Currency>>
    suspend fun getLatestRates(): Resource<ExchangeRateResponseDto>
    suspend fun getHistoricalRates(
        from: String,
        to: String,
        date: String
    ): Resource<List<HistoricalRate>>
}