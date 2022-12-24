package com.andela.currencyconverter.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andela.currencyconverter.data.DataState
import com.andela.currencyconverter.data.remote.responses.currency_converter.CurrencyConvertedResponse
import com.andela.currencyconverter.data.usecase.ConvertCurrencyUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    private val convertCurrencyUsecase: ConvertCurrencyUsecase
) : ViewModel() {

    private var _uiState = MutableLiveData<CurrencyConverterUiState>()
    var uiStateLiveData: LiveData<CurrencyConverterUiState> = _uiState

    private var _currencyConvertedResponse = MutableLiveData<CurrencyConvertedResponse>()
    var currencyConvertedLiveData: LiveData<CurrencyConvertedResponse> = _currencyConvertedResponse

    fun convertCurrency() {
             _uiState.postValue(LoadingState)
             viewModelScope.launch {
                 convertCurrencyUsecase().collect { dataState ->
                     when (dataState) {
                         is DataState.Success -> {
                             _uiState.postValue(ContentState)
                             _currencyConvertedResponse.postValue(dataState.data)
                         }

                         is DataState.Error -> {
                             _uiState.postValue(ErrorState(dataState.message))
                         }
                     }
             }
         }
    }
}
