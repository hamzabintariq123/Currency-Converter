package com.andela.currencyconverter.ui.currency_converter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.andela.currencyconverter.R
import com.andela.currencyconverter.databinding.ActivityMainBinding
import com.andela.currencyconverter.ui.currency_converter.converter.ContentState as ConverterContentState
import com.andela.currencyconverter.ui.currency_converter.converter.CurrencyConverterViewModel
import com.andela.currencyconverter.ui.currency_converter.symbols.ContentState as SymbolsContentState
import com.andela.currencyconverter.ui.currency_converter.converter.ErrorState as ConverterErrorState
import com.andela.currencyconverter.ui.currency_converter.converter.LoadingState as ConverterLoadingState
import com.andela.currencyconverter.ui.currency_converter.symbols.CurrencySymbolsViewModel
import com.andela.currencyconverter.ui.currency_converter.symbols.ErrorState as SymbolsErrorState
import com.andela.currencyconverter.ui.currency_converter.symbols.LoadingState as SymbolsLoadingState
import com.andela.currencyconverter.utils.gone
import com.andela.currencyconverter.utils.showSnack
import com.andela.currencyconverter.utils.visible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CurrencyConverterActivity : AppCompatActivity() {

    private val converterViewModel: CurrencyConverterViewModel by viewModels()
    private val symbolsViewModel: CurrencySymbolsViewModel by viewModels()

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@CurrencyConverterActivity, R.layout.activity_main)

        binding.lifecycleOwner = this
        binding.viewModel = symbolsViewModel

        symbolsViewModel.getCurrencySymbols()
        initObservations()
    }

    private fun initObservations(){
        converterViewModel.uiStateLiveData.observe(this) { state ->
            when (state) {
                is ConverterLoadingState -> {
                    binding.progressLogin.visible()
                }

                is ConverterContentState -> {
                    binding.progressLogin.gone()
                }

                is ConverterErrorState -> {
                    binding.progressLogin.gone()
                    binding.root.showSnack(state.message)
                }

                else->{}
            }
        }

        converterViewModel.currencyConvertedLiveData.observe(this){
            Log.d("res",it.result.toString())
        }

    }
}
