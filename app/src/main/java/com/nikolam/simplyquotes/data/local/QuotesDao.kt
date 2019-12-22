package com.nikolam.simplyquotes.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nikolam.simplyquotes.data.model.Quote


@Dao
interface QuotesDao {
    @Query("SELECT * FROM quotes") // get all locally saved quotes from the DB
    suspend fun getQuotes(): List<Quote>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun insertQuotes(quotes: List<Quote>)

    @Query("UPDATE quotes SET favorite = 1 WHERE entryid = :quoteID")
    suspend fun favoriteAQuote(quoteID: Int)

    @Query("SELECT * FROM quotes WHERE favorite = 1") // get all locally saved quotes from the DB
    suspend fun getFavoriteQuotes(): List<Quote>

    @Query("UPDATE quotes SET favorite = 0 WHERE entryid = :quoteID")
    suspend fun unfavoriteAQuote(quoteID: Int)

    @Query("UPDATE quotes SET favorite = 0 WHERE favorite = 1")
    suspend fun unfavoriteAllQuote()

}