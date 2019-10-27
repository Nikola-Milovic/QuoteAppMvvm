package com.example.quoteappmvvm.data.local

import androidx.room.Dao
import androidx.room.Query
import com.example.quoteappmvvm.data.model.Quote


@Dao
interface QuotesDao {
    @Query("SELECT * FROM quotes")
    suspend fun getQuotes(): List<Quote>
}