package com.andela.currencyconverter.data.repository.currency_repository

import com.andela.currencyconverter.data.remote.responses.currency_converter.CurrencyConvertedResponse
import com.andela.currencyconverter.data.DataState
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun convertCurrency(): Flow<DataState<CurrencyConvertedResponse>>
}
