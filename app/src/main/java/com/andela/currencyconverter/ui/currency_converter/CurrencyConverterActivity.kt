package com.andela.currencyconverter.ui.currency_converter

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.andela.currencyconverter.R
import com.andela.currencyconverter.databinding.ActivityMainBinding
import com.andela.currencyconverter.ui.currency_converter.converter.CurrencyConverterViewModel
import com.andela.currencyconverter.ui.currency_converter.symbols.CurrencySymbolsViewModel
import com.andela.currencyconverter.ui.details.DetailsActivity
import com.andela.currencyconverter.utils.extensions.debounce
import com.andela.currencyconverter.utils.gone
import com.andela.currencyconverter.utils.showSnack
import com.andela.currencyconverter.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import com.andela.currencyconverter.ui.currency_converter.converter.ContentState as ConverterContentState
import com.andela.currencyconverter.ui.currency_converter.converter.ErrorState as ConverterErrorState
import com.andela.currencyconverter.ui.currency_converter.converter.LoadingState as ConverterLoadingState
import com.andela.currencyconverter.ui.currency_converter.symbols.ContentState as SymbolsContentState
import com.andela.currencyconverter.ui.currency_converter.symbols.ErrorState as SymbolsErrorState
import com.andela.currencyconverter.ui.currency_converter.symbols.LoadingState as SymbolsLoadingState


@AndroidEntryPoint
class CurrencyConverterActivity : AppCompatActivity() {

    private val converterViewModel: CurrencyConverterViewModel by viewModels()
    private val symbolsViewModel: CurrencySymbolsViewModel by viewModels()
    lateinit var binding: ActivityMainBinding

    private var fromItem = 0
    private var toItem = 0
    private var debounceDelay = 500L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@CurrencyConverterActivity, R.layout.activity_main
        )

        binding.lifecycleOwner = this
        binding.viewModelSymbols = symbolsViewModel
        binding.viewModelCurrency = converterViewModel

        symbolsViewModel.getCurrencySymbols()
        initObservations()

        binding.convertFromEdt.debounce(debounceDelay) { text ->
            try {
                if ((text?.length ?: 0) > 0 && binding.convertFromEdt.hasFocus()) {
                    callConvertCurrency(
                        binding.convertFromEdt.text.toString().toDouble(),
                        symbolsViewModel.selectedFromItem.text,
                        symbolsViewModel.selectedToItem.text,
                        true
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.convertToEdt.debounce(debounceDelay) { text ->
            try {
                if ((text?.length ?: 0) > 0 && binding.convertToEdt.hasFocus()) {
                    callConvertCurrency(
                        binding.convertToEdt.text.toString().toDouble(),
                        symbolsViewModel.selectedToItem.text,
                        symbolsViewModel.selectedFromItem.text,
                        false
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.exchangeValues.setOnClickListener {
            binding.convertFrom.setSelection(toItem)
            binding.convertTo.setSelection(fromItem)

            callConvertCurrency(
                binding.convertFromEdt.text.toString().toDouble(),
                symbolsViewModel.selectedFromItem.text,
                symbolsViewModel.selectedToItem.text,
                true
            )
        }

        binding.buttonDetails.setOnClickListener{
            startActivity(Intent(this@CurrencyConverterActivity, DetailsActivity::class.java))
        }
    }

    private fun initObservations() {
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

                else -> {}
            }
        }

        symbolsViewModel.selectedFromItemIndex.observe(this) {
            fromItem = it
            callConvertCurrency(
                binding.convertFromEdt.text.toString().toDouble(),
                symbolsViewModel.selectedFromItem.text,
                symbolsViewModel.selectedToItem.text,
                true
            )
        }

        symbolsViewModel.selectedToItemIndex.observe(this) {
            toItem = it
            callConvertCurrency(
                binding.convertToEdt.text.toString().toDouble(),
                symbolsViewModel.selectedToItem.text,
                symbolsViewModel.selectedFromItem.text,
                false
            )
        }

        symbolsViewModel.uiStateLiveData.observe(this) { state ->
            when (state) {
                is SymbolsLoadingState -> {
                    binding.progressLogin.visible()
                }

                is SymbolsContentState -> {
                    binding.progressLogin.gone()
                }

                is SymbolsErrorState -> {
                    binding.progressLogin.gone()
                    binding.root.showSnack(state.message)
                }

                else -> {}
            }
        }

    }

    private fun callConvertCurrency(
        amount: Double,
        fromCurrency: String,
        toCurrency: String,
        isFrom: Boolean
    ) {
        if (binding.convertFromEdt.text.toString()
                .isNotEmpty() || binding.convertToEdt.text.toString().isNotEmpty()
        ) {
            convertCurrency(
                fromCurrency,
                toCurrency,
                amount,
                isFrom
            )

        } else {
            binding.root.showSnack(getString(R.string.enter_both_currencies_error))
        }
    }

    private fun convertCurrency(from: String, to: String, amount: Double, isFrom: Boolean) {
        converterViewModel.convertCurrency(from, to, amount, isFrom)
    }
}
