package com.andela.currencyconverter.ui.details

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.andela.currencyconverter.R
import com.andela.currencyconverter.adapter.HistoryRecyclerViewAdapter
import com.andela.currencyconverter.data.db.model.ConverterData
import com.andela.currencyconverter.databinding.ActivityDetailsBinding
import com.andela.currencyconverter.ui.details.details.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private val detailsViewModel: DetailsViewModel by viewModels()
    private lateinit var binding : ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this@DetailsActivity, R.layout.activity_details
        )

        detailsViewModel.convertCurrency()

        initObservations()
    }

    private fun initObservations() {
        detailsViewModel.historyList.observe(this){
          initHistoryDataRv(it)
        }
    }

    private fun initHistoryDataRv(converterDataList: MutableList<ConverterData>){
        val recyclerViewHistory = binding.historyRv
        val historyAdapter = HistoryRecyclerViewAdapter(converterDataList)

        recyclerViewHistory.adapter = historyAdapter
        recyclerViewHistory.layoutManager = LinearLayoutManager(this)
        recyclerViewHistory.setHasFixedSize(true)
    }
}