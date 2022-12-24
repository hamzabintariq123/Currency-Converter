package com.andela.currencyconverter.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

class BindableSpinnerAdapter(
    context: Context,
    textViewResourceId: Int,
    private val values: List<SpinnerItem>
) : ArrayAdapter<BindableSpinnerAdapter.SpinnerItem>(context, textViewResourceId, values) {

    override fun getCount() = values.size

    override fun getItem(position: Int) = values[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        label.text = values[position].text
        return label
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        label.text = values[position].text
        return label
    }


    data class SpinnerItem(val text: String)


    companion object {

        @BindingAdapter(
            value = ["spinnerItems", "selectedSpinnerItem", "selectedSpinnerItemAttrChanged"],
            requireAll = false
        )
        @JvmStatic
        fun setSpinnerItems(
            spinner: Spinner,
            spinnerItems: List<SpinnerItem>?,
            selectedSpinnerItem: SpinnerItem?,
            listener: InverseBindingListener?
        ) {
            val selectedItem = (spinner.selectedItem as? SpinnerItem)
            if (selectedItem != null && selectedSpinnerItem == selectedItem) {
                return
            }

            spinnerItems?.let {
                spinner.adapter = BindableSpinnerAdapter(
                    spinner.context,
                    android.R.layout.simple_spinner_dropdown_item,
                    it
                )
                setCurrentSelection(spinner, selectedSpinnerItem)
                setSpinnerListener(spinner, listener)
            }
        }

        @InverseBindingAdapter(attribute = "selectedSpinnerItem")
        @JvmStatic
        fun getSelectedSpinnerItem(spinner: Spinner): SpinnerItem {
            return spinner.selectedItem as SpinnerItem
        }

        @JvmStatic
        private fun setSpinnerListener(spinner: Spinner, listener: InverseBindingListener?) {
            listener?.let {
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) = it.onChange()

                    override fun onNothingSelected(adapterView: AdapterView<*>) = it.onChange()
                }
            }
        }

        @JvmStatic
        private fun setCurrentSelection(spinner: Spinner, selectedItem: SpinnerItem?): Boolean {
            selectedItem?.let {
                for (index in 0 until spinner.adapter.count) {
                    if (spinner.getItemAtPosition(index) == it.text) {
                        spinner.setSelection(index)
                        return true
                    }
                }
            }

            return false
        }

    } // companion object

} // BindableSpinnerAdapter class