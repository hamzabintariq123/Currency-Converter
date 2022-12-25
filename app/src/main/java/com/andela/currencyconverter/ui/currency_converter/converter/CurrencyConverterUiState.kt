package com.andela.currencyconverter.ui.currency_converter.converter

sealed class CurrencyConverterUiState

object LoadingState : CurrencyConverterUiState()
object ContentState : CurrencyConverterUiState()
class ErrorState(val message: String) : CurrencyConverterUiState()
