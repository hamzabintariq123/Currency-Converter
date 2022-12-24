package com.andela.currencyconverter.data.remote.responses.currency_converter

data class Query(
    val amount: Int,
    val from: String,
    val to: String
)