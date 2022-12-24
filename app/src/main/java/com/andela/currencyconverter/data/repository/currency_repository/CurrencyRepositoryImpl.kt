package com.andela.currencyconverter.data.repository.currency_repository

import androidx.annotation.WorkerThread
import com.andela.currencyconverter.data.DataState
import com.andela.currencyconverter.data.remote.responses.currency_converter.CurrencyConvertedResponse
import com.andela.currencyconverter.data.remote.services.CurrencyApiService
import com.andela.currencyconverter.utils.StringUtils
import com.serengeti.getihub.data.remote.onExceptionSuspend
import com.serengeti.getihub.data.remote.onSuccessSuspend
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CurrencyRepositoryImpl @Inject constructor(
    private val stringUtils: StringUtils,
    private val apiService: CurrencyApiService
) : CurrencyRepository {

    @WorkerThread
    override suspend fun convertCurrency(
    ): Flow<DataState<CurrencyConvertedResponse>> {
        return flow {
            apiService.convertCurrency(to= "USD", from = "PKR", amount = 10).apply {
                this.onSuccessSuspend {
                    data?.let {
                        if(!data.success){
                            emit(DataState.Error(data.error!!.info))
                        }else{
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
}
