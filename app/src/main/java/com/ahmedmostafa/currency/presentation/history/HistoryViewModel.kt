package com.ahmedmostafa.currency.presentation.history
import android.os.Build
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedmostafa.currency.core.utils.Resource
import com.ahmedmostafa.currency.domain.usecase.GetHistoricalRatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistoricalRatesUseCase: GetHistoricalRatesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val TAG = "HistoryViewModel"

    private val _state = MutableStateFlow(HistoryState())
    val state = _state.asStateFlow()

    init {
        val fromCurrency = savedStateHandle.get<String>("fromCurrency") ?: "USD"
        val toCurrency = savedStateHandle.get<String>("toCurrency") ?: "EUR"

        _state.update { it.copy(
            fromCurrency = fromCurrency,
            toCurrency = toCurrency,
            isLoading = true
        ) }
        
        loadHistoricalRates()
    }

    private fun loadHistoricalRates() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = getHistoricalRatesUseCase(
                _state.value.fromCurrency,
                _state.value.toCurrency,
                getCurrentDate()
            )) {
                is Resource.Success -> {
                    _state.update { it.copy(
                        historicalRates = result.data,
                        isLoading = false,
                        error = ""
                    ) }
                }
                is Resource.Error -> {
                    _state.update { it.copy(
                        error = result.message,
                        isLoading = false
                    ) }
                }

                Resource.Loading -> {
                    _state.update { it.copy(
                        isLoading = true
                    ) }
                }
            }
        }
    }


    private fun getCurrentDate(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Use LocalDate for newer APIs
            LocalDate.now().toString()
        } else {
            // Use Calendar for older APIs
            val calendar = Calendar.getInstance()
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            formatter.format(calendar.time)
        }
    }
}