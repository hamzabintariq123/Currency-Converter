package com.andela.currencyconverter.data.remote.responses.currency_symbols

import com.andela.currencyconverter.data.remote.responses.Error

data class CurrencySymbolsResponse(
    val success: Boolean,
    val error: Error?= null,
    val symbols: Symbols
)