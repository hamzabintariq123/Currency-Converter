package com.andela.currencyconverter.data.remote.responses.currency_converter

data class CurrencyConvertedResponse(
    val date: String,
    val error: Error ?= null,
    val query: Query,
    val result: Double,
    val success: Boolean
)