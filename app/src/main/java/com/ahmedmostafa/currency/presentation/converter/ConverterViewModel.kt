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
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            fetchRates()
            fetchCurrencies()
        }
    }


    private suspend fun fetchCurrencies() {
        when (val result = getCurrenciesUseCase()) {
            is Resource.Success -> {
                _state.update {

                    it.copy(
                        currencies = result.data.associate { currency -> currency.code to currency.name },
                        error = "",
                        fromCurrency = Currency(name = result.data.associate { currency -> currency.code to currency.name }
                            .get("GBP") ?: "", code = "GBP")
                    )
                }
            }

            is Resource.Error -> {
                _state.update { it.copy(error = result.message) }
            }

            Resource.Loading -> {}
        }

    }

    private suspend fun fetchRates() {
        when (val result = getLatestRatesUseCase()) {
            is Resource.Success -> {
                _state.update {
                    it.copy(
                        rates = result.data,
                        error = "",
                        isLoading = false
                    )
                }
            }

            is Resource.Error -> {
                _state.update { it.copy(error = result.message, isLoading = false) }
            }

            Resource.Loading -> {}
        }
    }

    fun setFromCurrency(currency: Currency) {
        _state.update { it.copy(fromCurrency = currency) }
        convertCurrency(
            fromCurrency = _state.value.fromCurrency.code,
            toCurrency = _state.value.toCurrency.code,
            amount = _state.value.amount.toDoubleOrNull() ?: 0.0,
            rates = _state.value.rates
        )
    }

    fun setToCurrency(currency: Currency) {
        _state.update { it.copy(toCurrency = currency) }
        convertCurrency(
            fromCurrency = _state.value.fromCurrency.code,
            toCurrency = _state.value.toCurrency.code,
            amount = _state.value.amount.toDoubleOrNull() ?: 0.0,
            rates = _state.value.rates
        )
    }

    fun setAmount(amount: String) {
        _state.update { it.copy(amount = amount) }
        convertCurrency(
            fromCurrency = _state.value.fromCurrency.code,
            toCurrency = _state.value.toCurrency.code,
            amount = _state.value.amount.toDoubleOrNull() ?: 0.0,
            rates = _state.value.rates
        )
    }

    fun swapCurrencies() {
        _state.update {
            it.copy(
                fromCurrency = it.toCurrency,
                toCurrency = it.fromCurrency,
            )

        }
        convertCurrency(
            fromCurrency = _state.value.fromCurrency.code,
            toCurrency = _state.value.toCurrency.code,
            amount = _state.value.amount.toDoubleOrNull() ?: 0.0,
            rates = _state.value.rates
        )
    }

    private fun convertCurrency(
        fromCurrency: String,
        toCurrency: String,
        amount: Double,
        rates: Map<String, Double>
    ) {
        val resource = getExchangeRateUseCase(fromCurrency, toCurrency, rates)
        when (resource) {
            is Resource.Error -> {
                _state.update { it.copy(error = resource.message) }
            }

            Resource.Loading -> {
                _state.update { it }
            }

            is Resource.Success -> {
                val result = amount * resource.data.rate
                _state.update {
                    it.copy(convertedAmount = "%.2f".format(result))
                }
            }
        }
    }


}