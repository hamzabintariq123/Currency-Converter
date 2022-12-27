package com.andela.currencyconverter.ui.currency_converter.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andela.currencyconverter.data.DataState
import com.andela.currencyconverter.data.db.model.ConverterData
import com.andela.currencyconverter.data.remote.responses.currency_converter.CurrencyConvertedResponse
import com.andela.currencyconverter.data.repository.currency_history.CurrencyHistoryRepository
import com.andela.currencyconverter.data.usecase.converter.ConvertCurrencyUsecase
import com.andela.currencyconverter.utils.StringUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    private val convertCurrencyUsecase: ConvertCurrencyUsecase,
    private val currencyHistoryRepository: CurrencyHistoryRepository,
    private val stringUtils: StringUtils
) : ViewModel() {

    val resultFrom: MutableLiveData<Double> = MutableLiveData<Double>().apply { postValue(1.0) }
    val resultTo: MutableLiveData<Double> = MutableLiveData<Double>().apply { postValue(1.0) }

    val result: MutableLiveData<Double> = MutableLiveData()

    private var _uiState = MutableLiveData<CurrencyConverterUiState>()
    var uiStateLiveData: LiveData<CurrencyConverterUiState> = _uiState

    private var _currencyConvertedResponse = MutableLiveData<CurrencyConvertedResponse>()
    var currencyConvertedLiveData: LiveData<CurrencyConvertedResponse> = _currencyConvertedResponse

    fun convertCurrency(from: String, to: String, amount: Double, isFrom: Boolean) {
        _uiState.postValue(LoadingState)

        if (from == to) {
            _uiState.postValue(ErrorState(stringUtils.sameCurrencyError()))
            result.postValue(amount)
        }

        viewModelScope.launch {
            convertCurrencyUsecase(from, to, amount).collect { dataState ->
                when (dataState) {
                    is DataState.Success -> {
                        _uiState.postValue(ContentState)
                        _currencyConvertedResponse.postValue(dataState.data)
                        saveDataToLocalDb(dataState.data)
                        result.postValue(dataState.data.result)

                        if (!isFrom) {
                            resultFrom.postValue(dataState.data.result)
                        } else {
                            resultTo.postValue(dataState.data.result)
                        }
                    }

                    is DataState.Error -> {
                        _uiState.postValue(ErrorState(dataState.message))
                    }
                }
            }
        }
    }

    private fun saveDataToLocalDb(dataState: CurrencyConvertedResponse) {
        val data = ConverterData(
            from = dataState.query.from,
            to = dataState.query.to,
            amount = dataState.query.amount.toString(),
            result = dataState.result.toString(),
            date = dataState.date
        )
        viewModelScope.launch {
            currencyHistoryRepository.insert(data)
        }
    }
}
