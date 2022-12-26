package com.andela.currencyconverter.ui.currency_converter.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andela.currencyconverter.data.DataState
import com.andela.currencyconverter.data.remote.responses.currency_converter.CurrencyConvertedResponse
import com.andela.currencyconverter.data.usecase.converter.ConvertCurrencyUsecase
import com.andela.currencyconverter.data.db.model.ConverterData
import com.andela.currencyconverter.data.repository.currency_history.CurrencyHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    private val convertCurrencyUsecase: ConvertCurrencyUsecase,
    private val currencyHistoryRepository: CurrencyHistoryRepository
) : ViewModel() {

    val resultFrom: MutableLiveData<Double> = MutableLiveData<Double>().apply { postValue(1.0) }
    val resultTo: MutableLiveData<Double> = MutableLiveData<Double>().apply { postValue(1.0) }

    val result : MutableLiveData<Double> = MutableLiveData()

    private var _uiState = MutableLiveData<CurrencyConverterUiState>()
    var uiStateLiveData: LiveData<CurrencyConverterUiState> = _uiState

    private var _currencyConvertedResponse = MutableLiveData<CurrencyConvertedResponse>()
    var currencyConvertedLiveData: LiveData<CurrencyConvertedResponse> = _currencyConvertedResponse

    fun convertCurrency(from: String, to: String, amount: Double, isFrom: Boolean) {
        _uiState.postValue(LoadingState)
        viewModelScope.launch {
            convertCurrencyUsecase(from, to, amount).collect { dataState ->
                when (dataState) {
                    is DataState.Success -> {
                        _uiState.postValue(ContentState)
                        _currencyConvertedResponse.postValue(dataState.data)

                        result.postValue(dataState.data.result)
                        val data = ConverterData(from = dataState.data.query.from,
                            to = dataState.data.query.to,
                            amount = dataState.data.query.amount.toString(),
                            result = dataState.data.result.toString(),
                            date = dataState.data.date)
                        currencyHistoryRepository.insert(data)

                        if (!isFrom){
                            resultFrom.postValue(dataState.data.result)
                        }else{
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
}
