package com.ahmedmostafa.currency.domain.usecase

import com.ahmedmostafa.currency.core.utils.Resource
import com.ahmedmostafa.currency.data.mappers.HistoricalRateMapper.fromResponse
import com.ahmedmostafa.currency.domain.model.HistoricalRate
import com.ahmedmostafa.currency.domain.repository.CurrencyRepository
import javax.inject.Inject

class GetHistoricalRatesUseCase @Inject constructor(
    private val repository: CurrencyRepository,
) {

    suspend operator fun invoke(
        from: String,
        to: String,
        dates: List<String>
    ): Resource<List<HistoricalRate>> {
        return try {
            val historicalRates = mutableListOf<HistoricalRate>()
            dates.forEach { date ->
                when (val result = repository.getHistoricalRates(date)) {
                    is Resource.Success -> {
                        val historicalRate = fromResponse(result.data, from, to, date)
                        historicalRates.add(historicalRate)
                    }

                    is Resource.Error -> throw Exception(result.message)
                    is Resource.Loading -> throw Exception("Unexpected loading state during use case execution")
                }
            }

            Resource.Success(historicalRates)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }
}
