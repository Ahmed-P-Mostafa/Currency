package com.ahmedmostafa.currency.domain.repository

import com.ahmedmostafa.currency.core.utils.Resource
import com.ahmedmostafa.currency.domain.model.Currency

interface CurrencyRepository {
    suspend fun getCurrencies(): Resource<List<Currency>>
    suspend fun getLatestRates(): Resource< Map<String, Double>>
    suspend fun getHistoricalRates(
        date: String
    ): Resource< Map<String, Double>>
}