package com.andela.currencyconverter.data.remote.responses.currency_converter

data class Error(
    val code: Int,
    val info: String,
    val type: String
)