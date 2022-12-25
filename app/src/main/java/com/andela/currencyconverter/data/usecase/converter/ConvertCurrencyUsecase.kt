package com.andela.currencyconverter.data.usecase.converter

import com.andela.currencyconverter.data.repository.currency_repository.CurrencyRepository
import javax.inject.Inject

class ConvertCurrencyUsecase @Inject constructor(private val repository: CurrencyRepository) {
    suspend operator fun invoke(from : String, to : String, amount : Double) = repository.convertCurrency(from, to, amount)
}
