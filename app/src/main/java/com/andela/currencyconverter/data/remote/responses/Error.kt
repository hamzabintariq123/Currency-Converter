package com.andela.currencyconverter.data.remote.responses

data class Error(
    val code: Int,
    val info: String,
    val type: String
)