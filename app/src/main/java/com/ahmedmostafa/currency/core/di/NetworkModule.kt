package com.ahmedmostafa.currency.core.di

import com.ahmedmostafa.currency.BuildConfig
import com.ahmedmostafa.currency.data.api.FixerApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor { chain ->
                val original = chain.request()
                val url = original.url.newBuilder()
                    .addQueryParameter("access_key", BuildConfig.API_ACCESS_KEY)
                    .build()
                chain.proceed(
                    original.newBuilder()
                        .url(url)
                        .build()
                )
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideFixerApi(client: OkHttpClient): FixerApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FixerApi::class.java)
    }
}