package com.example.quoteappmvvm.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quoteappmvvm.data.model.Quote


@Dao
interface QuotesDao {
    @Query("SELECT * FROM quotes") // get all locally saved quotes from the DB
    suspend fun getQuotes(): List<Quote>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotes(quotes: List<Quote>)

}