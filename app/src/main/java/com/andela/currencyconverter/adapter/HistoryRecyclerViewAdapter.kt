package com.andela.currencyconverter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andela.currencyconverter.data.db.model.ConverterData
import com.andela.currencyconverter.ui.details.ContentItem
import com.andela.currencyconverter.ui.details.SectionItem
import com.andela.currencyconverter.databinding.ItemHistoryDetailsBinding
import com.andela.currencyconverter.databinding.ItemSectionHeaderBinding
import com.andela.currencyconverter.ui.details.HistoryRvItems

const val VIEW_TYPE_SECTION = 1
const val VIEW_TYPE_ITEM = 2

class HistoryRecyclerViewAdapter(converterDataList: MutableList<ConverterData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var datesList = ArrayList<String>()
    private var dataSet = ArrayList<HistoryRvItems>()

    init {
        converterDataList.sortByDescending { it.date }
        datesList = converterDataList.distinctBy { it.date }.map { it.date } as ArrayList<String>
        for (i in 0 until datesList.size) {
            dataSet.add(SectionItem(datesList[i]))
            converterDataList.forEach {
                if (it.date == datesList[i]) {
                    dataSet.add(ContentItem(it))
                }
            }
        }
    }

    class HistoryViewHolder(
        private val binding: ItemHistoryDetailsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(converterData: ConverterData) {
            binding.historyObj = converterData
        }
    }

    class SectionViewHolder(
        private val binding: ItemSectionHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String) {
            binding.dateObj = title
        }
    }

    private lateinit var binding: ItemHistoryDetailsBinding
    private lateinit var bindingSectionHeader: ItemSectionHeaderBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_SECTION) {
            bindingSectionHeader =
                ItemSectionHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SectionViewHolder(bindingSectionHeader)
        }
        binding =
            ItemHistoryDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataSet[position]
        if (holder is SectionViewHolder && item is SectionItem) {
            holder.bind(item.title)
        }
        if (holder is HistoryViewHolder && item is ContentItem) {
            holder.bind(item.converterData)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (dataSet[position] is SectionItem) {
            return VIEW_TYPE_SECTION
        }
        return VIEW_TYPE_ITEM
    }

    override fun getItemCount() = dataSet.size

}
