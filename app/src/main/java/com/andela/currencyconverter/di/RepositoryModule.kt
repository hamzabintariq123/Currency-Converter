package com.andela.currencyconverter.di

import android.app.Application
import android.content.Context
import com.andela.currencyconverter.data.remote.services.CurrencyApiService
import com.andela.currencyconverter.data.repository.currency_repository.CurrencyRepository
import com.andela.currencyconverter.data.repository.currency_repository.CurrencyRepositoryImpl
import com.andela.currencyconverter.data.db.dao.ConverterDao
import com.andela.currencyconverter.data.db.CurrencyConverterDb
import com.andela.currencyconverter.data.repository.currency_history.CurrencyHistoryRepository
import com.andela.currencyconverter.utils.StringUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    fun provideCurrencyConverterDb(@ApplicationContext appContext: Context) : ConverterDao {
        return CurrencyConverterDb.getInstance(appContext).converterDao
    }

    @Provides
    fun provideRoomDBRepository(converterDao: ConverterDao) = CurrencyHistoryRepository(converterDao)
}
