package com.ahmedmostafa.currency.domain.repository;

import com.ahmedmostafa.currency.core.utils.Resource
import com.ahmedmostafa.currency.domain.model.Currency
import com.ahmedmostafa.currency.domain.model.ExchangeRate
import com.ahmedmostafa.currency.domain.model.HistoricalRate
import java.time.LocalDate

interface CurrencyRepository {
    suspend fun getCurrencies(): Resource<List<Currency>>
    suspend fun getExchangeRate(from: String, to: String): Resource<ExchangeRate>
    suspend fun getHistoricalRates(
        from: String,
        to: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Resource<List<HistoricalRate>>
}