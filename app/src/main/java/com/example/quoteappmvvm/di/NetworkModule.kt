package com.example.quoteappmvvm.di

import com.example.quoteappmvvm.data.model.QuoteApi
import com.example.quoteappmvvm.data.network.JsonNetworkService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Json
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

    @Provides
    fun provideRetrofitService(): Retrofit {
         val okHttpClient = OkHttpClient().newBuilder()
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://raw.githubusercontent.com/JamesFT/Database-Quotes-JSON/master/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }
}