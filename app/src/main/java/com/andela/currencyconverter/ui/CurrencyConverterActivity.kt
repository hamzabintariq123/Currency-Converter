package com.andela.currencyconverter.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.andela.currencyconverter.R
import com.andela.currencyconverter.databinding.ActivityMainBinding
import com.andela.currencyconverter.utils.gone
import com.andela.currencyconverter.utils.showSnack
import com.andela.currencyconverter.utils.visible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CurrencyConverterActivity : AppCompatActivity() {

    private val viewModel: CurrencyConverterViewModel by viewModels()

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@CurrencyConverterActivity, R.layout.activity_main)

        binding.lifecycleOwner = this

        viewModel.convertCurrency()
        initObservations()
    }

    private fun initObservations(){
        viewModel.uiStateLiveData.observe(this) { state ->
            when (state) {
                is LoadingState -> {
                    binding.progressLogin.visible()
                }

                is ContentState -> {
                    binding.progressLogin.gone()
                }

                is ErrorState -> {
                    binding.progressLogin.gone()
                    binding.root.showSnack(state.message)
                }

                else->{}
            }
        }

        viewModel.currencyConvertedLiveData.observe(this){
            Log.d("res",it.result.toString())
        }
    }
}
