package com.ahmedmostafa.currency.domain.usecase

import com.ahmedmostafa.currency.core.utils.Resource
import com.ahmedmostafa.currency.domain.model.Currency
import com.ahmedmostafa.currency.domain.repository.CurrencyRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetCurrenciesUseCaseTest {
    private lateinit var repository: CurrencyRepository
    private lateinit var useCase: GetCurrenciesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetCurrenciesUseCase(repository)
    }

    @Test
    fun `invoke returns success with currencies`() = runBlocking {
        // Given
        val currencies = listOf(
            Currency("USD", "US Dollar"),
            Currency("EUR", "Euro")
        )
        coEvery { repository.getCurrencies() } returns Resource.Success(currencies)

        // When
        val result = useCase()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(currencies, (result as Resource.Success).data)
    }

    @Test
    fun `invoke returns error when repository fails`() = runBlocking {
        // Given
        val errorMessage = "Network error"
        coEvery { repository.getCurrencies() } returns Resource.Error(errorMessage)

        // When
        val result = useCase()

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)
    }
}