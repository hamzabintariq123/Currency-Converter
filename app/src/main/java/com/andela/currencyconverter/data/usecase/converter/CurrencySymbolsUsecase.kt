package com.andela.currencyconverter.data.usecase.converter

import com.andela.currencyconverter.data.repository.currency_repository.CurrencyRepository
import javax.inject.Inject

class CurrencySymbolsUsecase @Inject constructor(private val repository: CurrencyRepository) {
    suspend operator fun invoke() = repository.getCurrencySymbols()
}
