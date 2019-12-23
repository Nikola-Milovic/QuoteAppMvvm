package com.nikolam.simplyquotes.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import retrofit2.http.GET


@Entity(tableName = "quotes")
data class Quote(
    @ColumnInfo(name = "quote_author") var quoteAuthor: String,
    @ColumnInfo(name = "quote_text") var quoteText: String,
    @ColumnInfo(name = "favorite") var isFavorite: Boolean = false,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "entryid") val id: Int
)

//A retrofit Network Interface for the Api
interface QuoteApi {
    @GET("quotes.json")
    suspend fun getQuotes(): List<Quote>
}