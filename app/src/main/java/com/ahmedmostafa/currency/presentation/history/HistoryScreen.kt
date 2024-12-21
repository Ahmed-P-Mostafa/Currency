package com.ahmedmostafa.currency.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmedmostafa.currency.R
import com.ahmedmostafa.currency.domain.model.ExchangeRate
import com.ahmedmostafa.currency.domain.model.HistoricalRate
import com.ahmedmostafa.currency.presentation.LoadingIndicator
import com.ahmedmostafa.currency.ui.theme.Green

@Composable
fun HistoryRoute(
    onNavigateBack: () -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    HistoryScreen(onNavigateBack, state)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun HistoryScreen(
    onNavigateBack: () -> Unit,
    state: HistoryState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.exchange_rate_history)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LazyColumn(
                    contentPadding = padding,
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .fillMaxWidth()
                ) {
                    items(state.historicalRates.size) { i ->
                        val rate = state.historicalRates[i]
                        RateHistoryCard(rate)
                    }
                }

                if (state.error.isNotBlank()) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    )
                }

            }

            LoadingIndicator(state.isLoading)

    }
}

@Composable
private fun RateHistoryCard(
    historicalRate: HistoricalRate,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                historicalRate.date,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceEvenly
            ) {
                HistoryCurrencyItem(
                    historicalRate.exchangeRate.fromCurrency,
                    "1.00"//the base value
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Navigate back"
                )
                HistoryCurrencyItem(
                    historicalRate.exchangeRate.toCurrency,
                    "%.2f".format(historicalRate.exchangeRate.rate)
                )

            }

        }

    }
}

@Composable
private fun HistoryCurrencyItem(
    currency: String,
    value: String
) {
    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(currency, style = MaterialTheme.typography.headlineLarge)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHistoryScreen() {
    val historicalRate = HistoricalRate(
        date = "20-12-2024",
        exchangeRate = ExchangeRate(
            fromCurrency = "EGP",
            toCurrency = "USD",
            rate = 50.05,
        )
    )

    HistoryScreen(
        {}, HistoryState(
            fromCurrency = "EGP",
            toCurrency = "USD",
            historicalRates = listOf(
                historicalRate,
                historicalRate,
                historicalRate,
                historicalRate
            ),
            isLoading = true,
        )
    )
}