package com.ahmedmostafa.currency.domain.usecase

import com.ahmedmostafa.currency.domain.repository.CurrencyRepository
import javax.inject.Inject

class GetExchangeRateUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(from: String, to: String) = 
        repository.getExchangeRate(from, to)
}