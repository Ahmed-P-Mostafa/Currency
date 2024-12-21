package com.ahmedmostafa.currency.domain.usecase

import com.ahmedmostafa.currency.core.utils.Resource
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test

class GetExchangeRateUseCaseTest {
    private lateinit var useCase: GetExchangeRateUseCase

    @Before
    fun setup() {
        useCase = GetExchangeRateUseCase()
    }

    @Test
    fun `calculate correct exchange rate for direct conversion`() {
        // Given
        val fromCurrency = "GBP"
        val toCurrency = "EUR"
        val rates = mapOf("EUR" to 1.0, "USD" to 1.25, "GBP" to 2.0)

        // When
        val result = useCase(fromCurrency, toCurrency, rates)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(0.5, (result as Resource.Success).data.rate)
    }

    @Test
    fun `return error for missing exchange rate`() {
        // Given
        val fromCurrency = "GBP"
        val toCurrency = "JPY"
        val rates = mapOf("EUR" to 1.15, "USD" to 1.25)

        // When
        val result = useCase(fromCurrency, toCurrency, rates)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Invalid fromCurrency", (result as Resource.Error).message)
    }
}