package com.ahmedmostafa.currency.domain.usecase


import com.ahmedmostafa.currency.core.utils.Resource
import com.ahmedmostafa.currency.domain.repository.CurrencyRepository
import io.mockk.coEvery
import io.mockk.mockk

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetLatestRatesUseCaseTest {
    private lateinit var repository: CurrencyRepository
    private lateinit var useCase: GetLatestRatesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetLatestRatesUseCase(repository)
    }

    @Test
    fun `successfully fetch latest rates`() = runBlocking {
        // Given
        val rates = mapOf("EUR" to 1.15, "USD" to 1.25)
        coEvery { repository.getLatestRates() } returns Resource.Success(rates)

        // When
        val result = useCase()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(rates, (result as Resource.Success).data)
    }

    @Test
    fun `handle error when fetching rates fails`() = runBlocking {
        // Given
        val errorMessage = "Network error"
        coEvery { repository.getLatestRates() } returns Resource.Error(errorMessage)

        // When
        val result = useCase()

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)
    }
}