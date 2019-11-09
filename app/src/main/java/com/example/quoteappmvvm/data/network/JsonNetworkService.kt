package com.example.quoteappmvvm.data.network

import com.example.quoteappmvvm.data.model.QuoteApi
import retrofit2.Retrofit
import javax.inject.Inject


class JsonNetworkService @Inject constructor(
    val retrofitService: Retrofit
) {

    fun getServiceApi(retrofit: Retrofit) = retrofit.create(QuoteApi::class.java)

    val apiService = getServiceApi(retrofitService)
}


