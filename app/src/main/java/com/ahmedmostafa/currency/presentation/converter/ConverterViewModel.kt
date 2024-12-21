package com.ahmedmostafa.currency.presentation.converter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedmostafa.currency.core.utils.Resource
import com.ahmedmostafa.currency.domain.model.Currency
import com.ahmedmostafa.currency.domain.usecase.GetCurrenciesUseCase
import com.ahmedmostafa.currency.domain.usecase.GetLatestRatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val getLatestRatesUseCase: GetLatestRatesUseCase
) : ViewModel() {

    private val TAG = "ConverterViewModel"

    private val _state = MutableStateFlow(ConverterState())
    val state = _state.asStateFlow()

    init {
        loadCurrencies()
        loadRates()
    }

    private fun loadCurrencies() {
        Log.e(TAG, "loadCurrencies: ${Calendar.getInstance().time}")
        viewModelScope.launch {
            when (val result = getCurrenciesUseCase()) {
                is Resource.Success -> {
                    _state.update {

                        it.copy(
                            currencies = result.data.associate { currency -> currency.code to currency.name },
                            error = "",
                            fromCurrency = Currency( name = result.data.associate { currency -> currency.code to currency.name }
                                .get("GBP")?:"", code = "GBP")
                        )
                    }
                }

                is Resource.Error -> {
                    _state.update { it.copy(error = result.message) }
                }

                Resource.Loading -> {}
            }
        }
    }

    private fun loadRates() {
        viewModelScope.launch {
            when (val result = getLatestRatesUseCase()) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            rates = result.data.rates,
                            error = ""
                        )
                    }
                }

                is Resource.Error -> {
                    _state.update { it.copy(error = result.message) }
                }

                Resource.Loading -> {}
            }
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
                /*amount = it.convertedAmount,
                convertedAmount = it.amount*/
            )

        }
        Log.d(TAG, "swapCurrencies:${_state.value.fromCurrency} ")
        Log.d(TAG, "swapCurrencies:${_state.value.toCurrency} ")
        Log.d(TAG, "swapCurrencies:${_state.value.amount} ")
        Log.d(TAG, "swapCurrencies:${_state.value.convertedAmount} ")
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
        Log.d(TAG, "convertCurrency: from $fromCurrency")
        Log.d(TAG, "convertCurrency: to $toCurrency")
        try {
            val rate = calculateConversionRate(fromCurrency, toCurrency, rates)
            val result = amount * rate
           // convertedAmount = "%.2f".format(convertedAmount)
            _state.update {
                it.copy(convertedAmount = "%.2f".format(result))
            }
        } catch (e: Exception) {
            _state.update { it.copy(error = e.localizedMessage ?: "Invalid Currency") }
        }

    }

    private fun calculateConversionRate(
        fromCurrency: String,
        toCurrency: String,
        rates: Map<String, Double>
    ): Double {
        Log.d(TAG, "calculateConversionRate: ${rates[fromCurrency]}")
        Log.d(TAG, "calculateConversionRate: ${rates[toCurrency]}")
        Log.d(TAG, "calculateConversionRate: ${rates}")
        val fromRate = rates[fromCurrency] ?: throw IllegalArgumentException("Invalid fromCurrency")
        val toRate = rates[toCurrency] ?: throw IllegalArgumentException("Invalid toCurrency")
        return toRate / fromRate
    }
}