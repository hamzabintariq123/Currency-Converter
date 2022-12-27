package com.andela.currencyconverter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andela.currencyconverter.data.db.model.ConverterData
import com.andela.currencyconverter.databinding.ItemHistoryDetailsBinding

class HistoryRecyclerViewAdapter(private val converterDataList: MutableList<ConverterData>): RecyclerView.Adapter<HistoryRecyclerViewAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(
        private val binding: ItemHistoryDetailsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(converterData: ConverterData) {
            binding.historyObj = converterData
        }
    }

    private lateinit var binding: ItemHistoryDetailsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        binding = ItemHistoryDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val largeNews = converterDataList[position]
        holder.bind(largeNews)
    }

    override fun getItemCount() = converterDataList.size

}
