package com.example.quoteappmvvm.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import java.util.*


@Entity(tableName = "quotes")
data class Quote(
    @ColumnInfo(name = "quote_author") var quoteAuthor: String = "",
    @ColumnInfo(name = "quote_text") var quoteText: String = "",
    @PrimaryKey @ColumnInfo(name = "entryid") var id: String = UUID.randomUUID().toString()
)

//A retrofit Network Interface for the Api
interface QuoteApi{
    @GET("quotes.json")
    //fun getQuotes(): Deferred<Response<QuoteResponse>>
    fun getQuotes(): Deferred<Response<List<Quote>>>
}