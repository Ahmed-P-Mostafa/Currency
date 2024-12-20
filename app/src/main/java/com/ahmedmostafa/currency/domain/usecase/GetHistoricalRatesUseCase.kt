package com.ahmedmostafa.currency.domain.usecase

import com.ahmedmostafa.currency.domain.repository.CurrencyRepository
import java.time.LocalDate
import javax.inject.Inject

class GetHistoricalRatesUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(
        from: String,
        to: String,
        startDate: LocalDate,
        endDate: LocalDate
    ) = repository.getHistoricalRates(from, to, startDate, endDate)
}