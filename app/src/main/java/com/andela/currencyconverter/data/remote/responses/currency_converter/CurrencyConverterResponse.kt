package com.andela.currencyconverter.data.remote.responses.currency_converter

import com.andela.currencyconverter.data.remote.responses.Error

data class CurrencyConvertedResponse(
    val date: String,
    val error: Error?= null,
    val query: Query,
    val result: Double,
    val success: Boolean
)