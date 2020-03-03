package com.nikolam.simplyquotes.data.network

import retrofit2.Retrofit
import javax.inject.Inject


class JsonNetworkService @Inject constructor(
    private val retrofitService: Retrofit
) {

    private fun getServiceApi(retrofit: Retrofit) = retrofit.create(QuoteApi::class.java)

    val apiService = getServiceApi(retrofitService)
}


