package com.andela.currencyconverter.ui.details.details

import android.icu.util.Calendar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andela.currencyconverter.data.db.model.ConverterData
import com.andela.currencyconverter.data.repository.currency_history.CurrencyHistoryRepository
import com.andela.currencyconverter.utils.extensions.dateFormat
import com.andela.currencyconverter.utils.extensions.toString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val currencyHistoryRepository: CurrencyHistoryRepository
) : ViewModel() {
    var historyList: LiveData<MutableList<ConverterData>> = MutableLiveData()
    var lastThreeDays = -3

    fun convertCurrency(){

        val calendar = Calendar.getInstance()

        val startDate = calendar.time.toString(dateFormat)
        calendar.add(Calendar.DATE,lastThreeDays)
        val endDate = calendar.time.toString(dateFormat)

        viewModelScope.launch {
            historyList = currencyHistoryRepository.fetch(endDate, startDate)
        }
    }
}
