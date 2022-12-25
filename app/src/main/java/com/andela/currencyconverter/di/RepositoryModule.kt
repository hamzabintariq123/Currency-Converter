package com.andela.currencyconverter.di

import android.app.Application
import com.andela.currencyconverter.data.remote.services.CurrencyApiService
import com.andela.currencyconverter.data.repository.currency_repository.CurrencyRepository
import com.andela.currencyconverter.data.repository.currency_repository.CurrencyRepositoryImpl
import com.andela.currencyconverter.utils.StringUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideStringUtils(app: Application): StringUtils {
        return StringUtils(app)
    }

    @Singleton
    @Provides
    fun provideCurrencyRepository(
        stringUtils: StringUtils,
        apiService: CurrencyApiService
    ): CurrencyRepository {
        return CurrencyRepositoryImpl(stringUtils, apiService)
    }
}
