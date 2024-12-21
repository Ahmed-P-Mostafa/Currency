package com.ahmedmostafa.currency.data.mappers

import com.ahmedmostafa.currency.core.utils.Utils.calculateConversionRate
import com.ahmedmostafa.currency.domain.model.ExchangeRate
import com.ahmedmostafa.currency.domain.model.HistoricalRate

object HistoricalRateMapper {

    fun fromResponse(
        rates: Map<String, Double>,
        from: String,
        to: String,
        currentDate: String
    ) = HistoricalRate(
        date = currentDate,
        exchangeRate = ExchangeRate(
            fromCurrency = from,
            toCurrency = to,
            rate = calculateConversionRate(
                fromCurrency = from,
                toCurrency = to,
                rates = rates
            )
        )
    )
}