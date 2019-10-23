package com.example.quoteappmvvm.data.network

import com.example.quoteappmvvm.data.model.QuoteApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



object JsonNetworkService {
    private val okHttpClient = OkHttpClient().newBuilder()
        .build()

    fun retrofit() : Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://raw.githubusercontent.com/JamesFT/Database-Quotes-JSON/master/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()


    val quoteApi : QuoteApi = retrofit().create(QuoteApi::class.java)

}


