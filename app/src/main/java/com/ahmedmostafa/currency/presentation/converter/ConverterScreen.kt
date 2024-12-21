package com.ahmedmostafa.currency.presentation.converter

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmedmostafa.currency.R
import com.ahmedmostafa.currency.domain.model.Currency
import com.ahmedmostafa.currency.ui.theme.Blue
import com.ahmedmostafa.currency.ui.theme.Cyan
import com.ahmedmostafa.currency.ui.theme.Gray
import kotlinx.coroutines.launch

@Composable
fun ConverterRoute(
    onNavigateToHistory: (String, String) -> Unit,
    viewModel: ConverterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    ConverterScreen(
        state = state,
        setFromCurrency = viewModel::setFromCurrency,
        setToCurrency = viewModel::setToCurrency,
        setAmount = viewModel::setAmount,
        swapCurrencies = viewModel::swapCurrencies,
        onNavigateToHistory = onNavigateToHistory
    )
}

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
private fun ConverterScreen(
    state: ConverterState,
    setFromCurrency: (Currency) -> Unit,
    setToCurrency: (Currency) -> Unit,
    setAmount: (String) -> Unit,
    swapCurrencies: () -> Unit,
    onNavigateToHistory: (String, String) -> Unit
) {
    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val historyClickErrorMessage = stringResource(R.string.history_click_error_message)
    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackBarHostState)
    }) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp, horizontal = screenWidth.times(0.1f)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.history),
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold),
                color = Cyan,
                modifier = Modifier
                    .clickable(onClick = {
                        if (state.fromCurrency.code.isNotBlank() && state.toCurrency.code.isNotBlank()) {
                            onNavigateToHistory(
                                state.fromCurrency.code,
                                state.toCurrency.code
                            )
                        } else {
                            scope.launch {
                                snackBarHostState.showSnackbar(historyClickErrorMessage)
                            }
                        }
                    })
                    .align(Alignment.End)
            )

            Text(
                text = stringResource(R.string.currencyConverter),
                color = Blue,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                ), modifier = Modifier.align(Alignment.Start)
            )

            CurrencyRow(
                currencies = state.currencies,
                selectedCurrency = state.fromCurrency.code,
                onCurrencySelected = setFromCurrency,
                label = "From",
                inputValue = state.amount,
                onValueChange = setAmount,

                )
            IconButton(
                onClick = swapCurrencies,
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Default.SwapVert,
                    contentDescription = "Swap currencies"
                )
            }

            CurrencyRow(
                currencies = state.currencies,
                selectedCurrency = state.toCurrency.code,
                onCurrencySelected = setToCurrency,
                label = "To",
                inputValue = state.convertedAmount,
                isEnabled = false
            )

            if (state.error.isNotBlank()) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


@Composable
private fun CurrencyRow(
    currencies: Map<String,String>,
    selectedCurrency: String,
    onCurrencySelected: (Currency) -> Unit,
    label: String,
    inputValue: String,
    onValueChange: (String) -> Unit = {},
    isEnabled: Boolean = true
) {
    Column {
        Text(label)
        Row(
            modifier = Modifier.fillMaxWidth(),

            ) {
            // Input Field
            CurrencyAmountInputField(
                state = inputValue,
                onValueChange =  onValueChange,
                isEnabled = isEnabled
            )
            VerticalDivider(Modifier.height(IntrinsicSize.Min))
            CurrencyDropdown(currencies, selectedCurrency, onCurrencySelected)

        }

    }
}

@Composable
private fun CurrencyAmountInputField(
    state: String,
    onValueChange: (String) -> Unit,
    isEnabled: Boolean = true
) {
    OutlinedTextField(
        value = state,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(0.3f),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Gray,
            unfocusedContainerColor = Gray,
            focusedBorderColor = Gray,
            unfocusedBorderColor = Gray,
            disabledContainerColor = Gray,
            disabledBorderColor = Gray,
            disabledTextColor = Color.Black
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        textStyle = TextStyle(fontSize = 18.sp),
        enabled = isEnabled
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencyDropdown(
    currencies: Map<String,String>,
    selectedCurrency: String,
    onCurrencySelected: (Currency) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        // Text(label)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth(1f),
        ) {
            OutlinedTextField(
                value = selectedCurrency,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor(),
                //  .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Gray,
                    unfocusedContainerColor = Gray,
                    focusedBorderColor = Gray,
                    unfocusedBorderColor = Gray,
                    disabledContainerColor = Gray,
                ), maxLines = 1
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                currencies.forEach { currency ->
                    DropdownMenuItem(
                        text = { Text(currency.value) },
                        onClick = {
                            onCurrencySelected(Currency(code = currency.key, name = currency.value))
                            expanded = false
                        }
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewConverterScreen() {
    ConverterScreen(
        state = ConverterState(
            currencies = mapOf("Bosnia-Herzegovina Convertible Mark" to ""),
            amount = "2.00"
        ),
        setFromCurrency = { },
        setToCurrency = {},
        setAmount = {},
        swapCurrencies = {},
        onNavigateToHistory = { _, _ -> }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewRow() {
    val state = ConverterState(toCurrency = Currency(code = "BCM",name = "Bosnia-Herzegovina Convertible Mark"))
    CurrencyRow(
        currencies = state.currencies,
        selectedCurrency = state.toCurrency.name,
        onCurrencySelected = {},
        label = stringResource(R.string.to),
        inputValue = state.convertedAmount,
        onValueChange = { },
    )
}
