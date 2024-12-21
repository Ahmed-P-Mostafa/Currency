package com.ahmedmostafa.currency.domain.usecase

import com.ahmedmostafa.currency.core.utils.Resource
import com.ahmedmostafa.currency.core.utils.Utils.calculateConversionRate
import com.ahmedmostafa.currency.domain.model.ExchangeRate

class GetExchangeRateUseCase {

    operator fun invoke(
        fromCurrency: String,
        toCurrency: String,
        rates: Map<String, Double>
    ): Resource<ExchangeRate> {
        return getExchangeRate(fromCurrency, toCurrency, rates)
    }


    private fun getExchangeRate(
        fromCurrency: String,
        toCurrency: String,
        rates: Map<String, Double>
    ): Resource<ExchangeRate> {

        return try {
            val rate = calculateConversionRate(fromCurrency, toCurrency, rates)
            Resource.Success(ExchangeRate(fromCurrency, toCurrency, rate))

        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error find exchange rate")
        }
    }


}