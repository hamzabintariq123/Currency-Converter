package com.andela.currencyconverter.ui.details.details

sealed class DetailsUiState

object LoadingState : DetailsUiState()
object ContentState : DetailsUiState()
class ErrorState(val message: String) : DetailsUiState()
