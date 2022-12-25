package com.andela.currencyconverter.data.repository.currency_repository

import androidx.annotation.WorkerThread
import com.andela.currencyconverter.adapter.BindableSpinnerAdapter
import com.andela.currencyconverter.data.DataState
import com.andela.currencyconverter.data.remote.responses.currency_converter.CurrencyConvertedResponse
import com.andela.currencyconverter.data.remote.responses.currency_symbols.CurrencySymbolsResponse
import com.andela.currencyconverter.data.remote.responses.currency_symbols.Symbols
import com.andela.currencyconverter.data.remote.services.CurrencyApiService
import com.andela.currencyconverter.utils.StringUtils
import com.serengeti.getihub.data.remote.onExceptionSuspend
import com.serengeti.getihub.data.remote.onSuccessSuspend
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject

class CurrencyRepositoryImpl @Inject constructor(
    private val stringUtils: StringUtils,
    private val apiService: CurrencyApiService
) : CurrencyRepository {

    @WorkerThread
    override suspend fun convertCurrency(
        from : String,
        to : String,
        amount : Double
    ): Flow<DataState<CurrencyConvertedResponse>> {
        return flow {
            apiService.convertCurrency(to = to, from = from, amount = amount.toInt()).apply {
                this.onSuccessSuspend {
                    data?.let {
                        if (!data.success) {
                            emit(DataState.Error(data.error!!.info))
                        } else {
                            emit(DataState.success(it))
                        }
                    }
                }
                // handle the case when the API request gets an error response.
                // e.g. internal server error.
            }.onExceptionSuspend {
                if (this.exception is IOException) {
                    emit(DataState.error(stringUtils.noNetworkErrorMessage()))
                } else {
                    emit(DataState.error(stringUtils.somethingWentWrong()))
                }
            }
        }
    }

    override suspend fun getCurrencySymbols(): Flow<DataState<CurrencySymbolsResponse>> {
        return flow {
            apiService.currencySymbols().apply {
                this.onSuccessSuspend {
                    data?.let {
                        val obj = response.body()?.string()?.let { it1 -> JSONObject(it1) }
                        emit(DataState.success(parseSymbolsJson(obj)))
                    }
                }
                // handle the case when the API request gets an error response.
                // e.g. internal server error.
            }.onExceptionSuspend {
                if (this.exception is IOException) {
                    emit(DataState.error(stringUtils.noNetworkErrorMessage()))
                } else {
                    emit(DataState.error(stringUtils.somethingWentWrong()))
                }
            }
        }
    }

    private fun parseSymbolsJson(json: JSONObject?): CurrencySymbolsResponse {
       val SUCCESS_KEY = "success"
       val SYMBOLS_KEYS = "symbols"

        val currencyList: ArrayList<BindableSpinnerAdapter.SpinnerItem> = ArrayList()
        var success = false

        json?.let {
            success = json.getBoolean(SUCCESS_KEY)
            val result = json.getJSONObject(SYMBOLS_KEYS)

            result.keys().forEach { keyStr ->
                currencyList.add(BindableSpinnerAdapter.SpinnerItem(keyStr))
            }
        }

        return CurrencySymbolsResponse(success, null, Symbols(currencyList))
    }
}
