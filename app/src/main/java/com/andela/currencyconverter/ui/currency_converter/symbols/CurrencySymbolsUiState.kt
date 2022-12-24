package com.andela.currencyconverter.ui.currency_converter.symbols

sealed class CurrencySymbolsUiState

object LoadingState : CurrencySymbolsUiState()
object ContentState : CurrencySymbolsUiState()
class ErrorState(val message: String) : CurrencySymbolsUiState()
