package com.ahmedmostafa.currency.data.repositiry

import com.ahmedmostafa.currency.core.utils.Resource
import com.ahmedmostafa.currency.core.utils.safeApiCall
import com.ahmedmostafa.currency.data.api.FixerApi
import com.ahmedmostafa.currency.domain.model.Currency
import com.ahmedmostafa.currency.domain.repository.CurrencyRepository
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val api: FixerApi
) : CurrencyRepository {

    override suspend fun getCurrencies(): Resource<List<Currency>> =
        safeApiCall(
            apiCall = { api.getCurrencies() },
            transform = { response ->
                response.symbols.map { (code, name) ->
                    Currency(code, name)
                }
            }
        )

    override suspend fun getLatestRates(): Resource<Map<String, Double>> =
        safeApiCall(
            apiCall = { api.getLatestRate() },
            transform = { it.rates }
        )

    override suspend fun getHistoricalRates(
        date: String
    ): Resource<Map<String, Double>> =
        safeApiCall(
            apiCall = { api.getHistoricalRates(date = date) },
            transform = { response ->
                response.rates
            }
        )
}