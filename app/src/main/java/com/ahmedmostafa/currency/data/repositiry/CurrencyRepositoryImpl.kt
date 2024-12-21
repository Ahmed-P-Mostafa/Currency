package com.ahmedmostafa.currency.data.repositiry

import com.ahmedmostafa.currency.core.utils.Resource
import com.ahmedmostafa.currency.data.api.FixerApi
import com.ahmedmostafa.currency.data.dto.ExchangeRateResponseDto
import com.ahmedmostafa.currency.domain.model.Currency
import com.ahmedmostafa.currency.domain.model.ExchangeRate
import com.ahmedmostafa.currency.domain.model.HistoricalRate
import com.ahmedmostafa.currency.domain.repository.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
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
                Resource.Error(response.error?.info ?: "Failed to fetch currencies")
            }
        } catch (e: UnknownHostException) {
            Resource.Error(e.message ?: "No Internet")
        }catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun getLatestRates(
    ): Resource<ExchangeRateResponseDto> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getLatestRate()
                if (response.success) {
                    Resource.Success(
                        response
                    )
                } else {
                    Resource.Error(response.error?.info ?: "Failed to fetch exchange rates")
                }
            } catch (e: UnknownHostException) {
                Resource.Error(e.message ?: "No Internet")
            }catch (e: Exception) {
                Resource.Error(e.message ?: "An unexpected error occurred")
            }
        }

    override suspend fun getHistoricalRates(
        from: String,
        to: String,
        date: String,
    ): Resource<List<HistoricalRate>> = withContext(Dispatchers.IO) {
        try {

            val response = api.getHistoricalRates(
                base = from,
                symbols = to,
                date = date
            )

            if (response.success) {
                Resource.Success(
                    response.rates.map { (date, rate) ->
                        HistoricalRate(
                            date = response.timestamp.toString(),
                            rate = ExchangeRate(
                                fromCurrency = response.base,
                                toCurrency = date,
                                rate = rate,
                                timestamp = response.timestamp.toLong()
                            )
                        )
                    }
                )
            } else {
                Resource.Error(response.error?.info ?:"Failed to fetch historical rates")
            }
        } catch (e: UnknownHostException) {
            Resource.Error(e.message ?: "No Internet")
        }catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }
}