package com.andela.currencyconverter.data.repository.currency_history

import com.andela.currencyconverter.data.db.dao.ConverterDao
import com.andela.currencyconverter.data.db.model.ConverterData
import javax.inject.Inject

class CurrencyHistoryRepository @Inject constructor(private val converterDao: ConverterDao) {
    suspend fun insert(converterData: ConverterData) = converterDao.insert(converterData)
    fun fetch(startDate: String, endDate: String) = converterDao.fetch(startDate, endDate)
}
