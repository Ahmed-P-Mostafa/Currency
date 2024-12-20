package com.ahmedmostafa.currency.data.repositiry

import android.os.Build
import com.ahmedmostafa.currency.core.utils.Resource
import com.ahmedmostafa.currency.data.api.FixerApi
import com.ahmedmostafa.currency.domain.model.Currency
import com.ahmedmostafa.currency.domain.model.ExchangeRate
import com.ahmedmostafa.currency.domain.model.HistoricalRate
import com.ahmedmostafa.currency.domain.repository.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val api: FixerApi
) : CurrencyRepository {

    override suspend fun getCurrencies(): Resource<List<Currency>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getCurrencies()
            if (response.success) {
                Resource.Success(
                    response.symbols.map { (code, name) ->
                        Currency(code, name)
                    }
                )
            } else {
                Resource.Error("Failed to fetch currencies")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun getExchangeRate(from: String, to: String): Resource<ExchangeRate> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getLatestRate()
                if (response.success) {
                    val rate = response.rates[to]
                    if (rate != null) {
                        Resource.Success(
                            ExchangeRate(
                                fromCurrency = from,
                                toCurrency = to,
                                rate = rate,
                                timestamp = response.timestamp
                            )
                        )
                    } else {
                        Resource.Error("Rate not found")
                    }
                } else {
                    Resource.Error("Failed to fetch exchange rate")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "An unexpected error occurred")
            }
        }

    override suspend fun getHistoricalRates(
        from: String,
        to: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Resource<List<HistoricalRate>> = withContext(Dispatchers.IO) {
        try {
            val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                DateTimeFormatter.ISO_DATE
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            val response = api.getHistoricalRates(
                base = from,
                symbols = to,
                startDate = startDate.format(formatter),
                endDate = endDate.format(formatter)
            )
            
            if (response.success) {
                Resource.Success(
                    response.rates.map { (date, rate) ->
                        HistoricalRate(
                            date = LocalDate.parse(response.timestamp.toString()),
                            rate = ExchangeRate(fromCurrency = response.base, toCurrency = date, rate = rate, timestamp = response.timestamp)
                        )
                    }
                )
            } else {
                Resource.Error("Failed to fetch historical rates")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }
}