package com.ahmedmostafa.currency.core.di

import com.ahmedmostafa.currency.data.api.FixerApi
import com.ahmedmostafa.currency.data.repositiry.CurrencyRepositoryImpl
import com.ahmedmostafa.currency.domain.repository.CurrencyRepository
import com.ahmedmostafa.currency.domain.usecase.GetCurrenciesUseCase
import com.ahmedmostafa.currency.domain.usecase.GetExchangeRateUseCase
import com.ahmedmostafa.currency.domain.usecase.GetHistoricalRatesUseCase
import com.ahmedmostafa.currency.domain.usecase.GetLatestRatesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideCurrencyRepository(api: FixerApi): CurrencyRepository {
        return CurrencyRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideGetCurrenciesUseCase(repository: CurrencyRepository): GetCurrenciesUseCase {
        return GetCurrenciesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetLatestRatesUseCase(repository: CurrencyRepository): GetLatestRatesUseCase {
        return GetLatestRatesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetHistoricalRatesUseCase(repository: CurrencyRepository): GetHistoricalRatesUseCase {
        return GetHistoricalRatesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetExchangeRateUseCase(): GetExchangeRateUseCase {
        return GetExchangeRateUseCase()
    }

}