package com.ahmedmostafa.currency.domain.usecase

import com.ahmedmostafa.currency.core.utils.Resource
import com.ahmedmostafa.currency.data.mappers.HistoricalRateMapper
import com.ahmedmostafa.currency.domain.model.HistoricalRate
import com.ahmedmostafa.currency.domain.repository.CurrencyRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class GetHistoricalRatesUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {
    private val HISTORY_DAYS: Long = 4
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    suspend operator fun invoke(
        from: String,
        to: String,
        date: String
    ): Resource<List<HistoricalRate>> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)

        val historicalRates = mutableListOf<HistoricalRate>()

        try {
            calendar.time = dateFormat.parse(date)!!

            for (i in 0 until HISTORY_DAYS) {
                val currentDate = dateFormat.format(calendar.time)
                val result = repository.getHistoricalRates(currentDate)

                when (result) {
                    is Resource.Success -> {
                        val historicalRate =
                            HistoricalRateMapper.fromResponse(result.data, from, to, currentDate)
                        historicalRates.add(historicalRate)
                    }

                    is Resource.Error -> {
                        return Resource.Error(result.message)
                    }

                    is Resource.Loading -> {
                        return Resource.Loading
                    }
                }
                calendar.add(Calendar.DAY_OF_YEAR, -1)
            }

            return Resource.Success(historicalRates)

        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Error parsing or processing dates")
        }
    }
}