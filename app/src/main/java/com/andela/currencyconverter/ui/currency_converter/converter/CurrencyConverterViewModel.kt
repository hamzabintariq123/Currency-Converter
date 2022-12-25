package com.andela.currencyconverter.ui.currency_converter.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andela.currencyconverter.data.DataState
import com.andela.currencyconverter.data.remote.responses.currency_converter.CurrencyConvertedResponse
import com.andela.currencyconverter.data.usecase.converter.ConvertCurrencyUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    private val convertCurrencyUsecase: ConvertCurrencyUsecase
) : ViewModel() {

    val resultFrom: MutableLiveData<Double> = MutableLiveData<Double>().apply { postValue(1.0) }
    val resultTo: MutableLiveData<Double> = MutableLiveData<Double>().apply { postValue(1.0) }

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
