package com.ahmedmostafa.currency.presentation.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedmostafa.currency.core.utils.Resource
import com.ahmedmostafa.currency.core.utils.getHistoryDaysDates
import com.ahmedmostafa.currency.domain.model.HistoricalRate
import com.ahmedmostafa.currency.domain.usecase.GetHistoricalRatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistoricalRatesUseCase: GetHistoricalRatesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state = _state.asStateFlow()

    init {
        val fromCurrency = savedStateHandle.get<String>("fromCurrency") ?: "USD"
        val toCurrency = savedStateHandle.get<String>("toCurrency") ?: "EUR"

        _state.update {
            it.copy(
                fromCurrency = fromCurrency,
                toCurrency = toCurrency,
                isLoading = true
            )
        }
        loadHistoricalRates(fromCurrency, toCurrency)
    }

    private fun loadHistoricalRates(fromCurrency: String, toCurrency: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = getHistoricalRatesUseCase(
                fromCurrency,
                toCurrency,
                getHistoryDaysDates()
            )
            updateState(result)
        }
    }

    private fun updateState(result: Resource<List<HistoricalRate>>) {
        when (result) {
            is Resource.Success -> {
                _state.update {
                    it.copy(
                        historicalRates = result.data,
                        isLoading = false,
                        error = ""
                    )
                }
            }

            is Resource.Error -> {
                _state.update {
                    it.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
            }

            Resource.Loading -> {
                _state.update {
                    it.copy(
                        isLoading = true
                    )
                }
            }
        }
    }
}