package com.andela.currencyconverter.di

import com.andela.currencyconverter.BuildConfig
import com.andela.currencyconverter.data.remote.services.CurrencyApiService
import com.andela.currencyconverter.utils.AppConstants
import com.serengeti.getihub.data.remote.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkApiModule {

    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val newRequest =
                    request.newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("apikey", AppConstants.API.API_KEY)
                chain.proceed(newRequest.build())
            }.also { client ->
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }
            }
            .addInterceptor(logging)
            .build()
    }

    @Singleton
    @Provides
    fun providesCurrencyApiService(okHttpClient: OkHttpClient): CurrencyApiService {
        return Retrofit.Builder()
            .baseUrl(CurrencyApiService.BASE_API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory())
            .build()
            .create(CurrencyApiService::class.java)
    }


}
