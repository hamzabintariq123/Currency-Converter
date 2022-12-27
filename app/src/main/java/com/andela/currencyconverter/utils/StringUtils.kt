package com.andela.currencyconverter.utils

import android.app.Application
import com.andela.currencyconverter.R

class StringUtils(val appContext: Application) {
    fun noNetworkErrorMessage() = appContext.getString(R.string.message_no_network_connected_str)
    fun somethingWentWrong() = appContext.getString(R.string.message_something_went_wrong_str)
    fun sameCurrencyError() = appContext.getString(R.string.same_currency_error)
}
