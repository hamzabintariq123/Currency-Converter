package com.andela.currencyconverter.data.remote.responses.currency_symbols

import com.andela.currencyconverter.adapter.BindableSpinnerAdapter

data class Symbols(
  val currencyList : List<BindableSpinnerAdapter.SpinnerItem>
)