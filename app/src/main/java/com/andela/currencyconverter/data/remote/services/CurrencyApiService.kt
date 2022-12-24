package com.andela.currencyconverter.data.remote.services

import com.andela.currencyconverter.data.remote.responses.currency_converter.CurrencyConvertedResponse
import com.andela.currencyconverter.data.remote.ApiResponse
import okhttp3.ResponseBody
import retrofit2.http.*

interface CurrencyApiService {

    @GET("fixer/convert")
    suspend fun convertCurrency(
        @Query("to") to: String,
        @Query("from") from: String,
        @Query("amount") amount: Int? = null,
    ): ApiResponse<CurrencyConvertedResponse>

    @GET("fixer/symbols")
    suspend fun currencySymbols(
    ): ApiResponse<ResponseBody>


    companion object {
        const val BASE_API_URL = "https://api.apilayer.com"
    }
}
