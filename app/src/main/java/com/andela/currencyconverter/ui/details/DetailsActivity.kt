package com.andela.currencyconverter.ui.details

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.andela.currencyconverter.databinding.ActivityDetailsBinding
import com.andela.currencyconverter.ui.details.details.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private val detailsViewModel: DetailsViewModel by viewModels()
    private lateinit var binding : ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)

        detailsViewModel.convertCurrency()

        initObservations()
    }

    private fun initObservations() {
        detailsViewModel.historyList.observe(this){
            it?.forEach { data->
                Log.d("res", data.result)
            }

        }
    }
}