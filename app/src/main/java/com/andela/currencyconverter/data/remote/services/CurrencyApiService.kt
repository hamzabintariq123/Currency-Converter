package com.andela.currencyconverter.data.remote.services

import com.andela.currencyconverter.data.remote.responses.currency_converter.CurrencyConvertedResponse
import com.serengeti.getihub.data.remote.ApiResponse
import retrofit2.http.*

interface CurrencyApiService {

    @GET("fixer/convert")
    suspend fun convertCurrency(
        @Query("to") to: String,
        @Query("from") from: String,
        @Query("amount") amount: Int? = null,
    ): ApiResponse<CurrencyConvertedResponse>


    companion object {
        const val BASE_API_URL = "https://api.apilayer.com"
    }
}
