package com.andela.currencyconverter.ui.currency_converter.symbols

import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andela.currencyconverter.adapter.BindableSpinnerAdapter.SpinnerItem
import com.andela.currencyconverter.data.DataState
import com.andela.currencyconverter.data.remote.responses.currency_symbols.CurrencySymbolsResponse
import com.andela.currencyconverter.data.usecase.converter.CurrencySymbolsUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencySymbolsViewModel @Inject constructor(
    private val currencySymbolsUsecase: CurrencySymbolsUsecase
) : ViewModel() {

    val items: MutableLiveData<List<SpinnerItem>> = MutableLiveData()
    var selectedToItem: SpinnerItem = SpinnerItem("")
    var selectedFromItem: SpinnerItem = SpinnerItem("")
    var selectedToItemIndex: MutableLiveData<Int> = MutableLiveData()
    var selectedFromItemIndex: MutableLiveData<Int> = MutableLiveData()

    private var _uiState = MutableLiveData<CurrencySymbolsUiState>()
    var uiStateLiveData: LiveData<CurrencySymbolsUiState> = _uiState

    private var _currencySymbolsResponse = MutableLiveData<CurrencySymbolsResponse>()

    fun getCurrencySymbols() {
        _uiState.postValue(LoadingState)
        viewModelScope.launch {
            currencySymbolsUsecase().collect { dataState ->
                when (dataState) {
                    is DataState.Success -> {
                        _uiState.postValue(ContentState)
                        _currencySymbolsResponse.postValue(dataState.data)
                        items.postValue(dataState.data.symbols.currencyList)
                    }

                    is DataState.Error -> {
                        _uiState.postValue(ErrorState(dataState.message))
                    }
                }
            }
        }
    }

    fun onFromSelectItem(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        selectedFromItemIndex.postValue(pos)
        selectedFromItem = parent?.selectedItem as SpinnerItem
    }

    fun onToSelectItem(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        selectedToItemIndex.postValue(pos)
        selectedToItem = parent?.selectedItem as SpinnerItem
    }
}
