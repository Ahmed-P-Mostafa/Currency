package com.ahmedmostafa.currency.domain.usecase

import com.ahmedmostafa.currency.domain.repository.CurrencyRepository
import javax.inject.Inject

class GetCurrenciesUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke() = repository.getCurrencies()
}