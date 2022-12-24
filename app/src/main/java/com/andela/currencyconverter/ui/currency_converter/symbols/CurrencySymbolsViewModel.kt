package com.andela.currencyconverter.ui.currency_converter.symbols

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andela.currencyconverter.data.DataState
import com.andela.currencyconverter.data.remote.responses.currency_symbols.CurrencySymbolsResponse
import com.andela.currencyconverter.data.usecase.converter.CurrencySymbolsUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@HiltViewModel
class CurrencySymbolsViewModel @Inject constructor(
    private val currencySymbolsUsecase: CurrencySymbolsUsecase
) : ViewModel() {

    private var _uiState = MutableLiveData<CurrencySymbolsUiState>()
    var uiStateLiveData: LiveData<CurrencySymbolsUiState> = _uiState

    private var _currencySymbolsdResponse = MutableLiveData<CurrencySymbolsResponse>()
    var currencySymbolsLiveData: LiveData<CurrencySymbolsResponse> = _currencySymbolsdResponse

    fun getCurrencySymbols() {
             _uiState.postValue(LoadingState)
             viewModelScope.launch {
                 currencySymbolsUsecase().collect { dataState ->
                     when (dataState) {
                         is DataState.Success -> {
                             _uiState.postValue(ContentState)
                             _currencySymbolsdResponse.postValue(dataState.data)
                         }

                         is DataState.Error -> {
                             _uiState.postValue(ErrorState(dataState.message))
                         }
                     }
             }
         }
    }
}
