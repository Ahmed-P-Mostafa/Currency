package com.ahmedmostafa.currency.presentation.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedmostafa.currency.core.utils.Resource
import com.ahmedmostafa.currency.domain.model.Currency
import com.ahmedmostafa.currency.domain.usecase.GetCurrenciesUseCase
import com.ahmedmostafa.currency.domain.usecase.GetExchangeRateUseCase
import com.ahmedmostafa.currency.domain.usecase.GetLatestRatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val getLatestRatesUseCase: GetLatestRatesUseCase,
    private val getExchangeRateUseCase: GetExchangeRateUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ConverterState())
    val state = _state.asStateFlow()

    init {
        initializeData()
    }

    private fun initializeData() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            fetchCurrencies()
            fetchRates()
        }
    }

    private suspend fun fetchCurrencies() {
        when (val result = getCurrenciesUseCase()) {
            is Resource.Success -> handleCurrenciesSuccess(result.data)
            is Resource.Error -> handleError(result.message)
            Resource.Loading -> {} // No specific action required
        }
    }

    private fun handleCurrenciesSuccess(currencies: List<Currency>) {
        val currencyMap = currencies.associate { it.code to it.name }
        val defaultCurrency = Currency(
            name = currencyMap["GBP"] ?: "",
            code = "GBP"
        )

        _state.update {
            it.copy(
                currencies = currencyMap,
                fromCurrency = defaultCurrency,
                error = "",
            )
        }
    }

    private suspend fun fetchRates() {
        when (val result = getLatestRatesUseCase()) {
            is Resource.Success -> handleRatesSuccess(result.data)
            is Resource.Error -> handleError(result.message)
            Resource.Loading -> {} // No specific action required
        }
    }

    private fun handleRatesSuccess(rates: Map<String, Double>) {
        _state.update {
            it.copy(
                rates = rates,
                error = "",
                isLoading = false
            )
        }
    }

    private fun handleError(message: String?) {
        _state.update { it.copy(error = message ?: "Unknown error", isLoading = false) }
    }

    fun setFromCurrency(currency: Currency) {
        updateCurrencyState { it.copy(fromCurrency = currency) }
    }

    fun setToCurrency(currency: Currency) {
        updateCurrencyState { it.copy(toCurrency = currency) }
    }

    fun setAmount(amount: String) {
        _state.update { it.copy(amount = amount) }
        performConversion()
    }

    fun swapCurrencies() {
        _state.update {
            it.copy(
                fromCurrency = it.toCurrency,
                toCurrency = it.fromCurrency,
            )
        }
        performConversion()
    }

    private fun updateCurrencyState(update: (ConverterState) -> ConverterState) {
        _state.update(update)
        performConversion()
    }

    private fun performConversion() {
        val currentState = _state.value
        val amount = currentState.amount.toDoubleOrNull() ?: 0.0
        convertCurrency(
            fromCurrency = currentState.fromCurrency.code,
            toCurrency = currentState.toCurrency.code,
            amount = amount,
            rates = currentState.rates
        )
    }

    private fun convertCurrency(
        fromCurrency: String,
        toCurrency: String,
        amount: Double,
        rates: Map<String, Double>
    ) {
        when (val resource = getExchangeRateUseCase(fromCurrency, toCurrency, rates)) {
            is Resource.Success -> handleConversionSuccess(amount, resource.data.rate)
            is Resource.Error -> handleError(resource.message)
            Resource.Loading -> {} // No specific action required
        }
    }

    private fun handleConversionSuccess(amount: Double, rate: Double) {
        val result = amount * rate
        _state.update {
            it.copy(convertedAmount = "%.2f".format(result))
        }
    }
}