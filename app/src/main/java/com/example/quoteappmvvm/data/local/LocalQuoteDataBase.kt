package com.example.quoteappmvvm.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quoteappmvvm.data.model.Quote

@Database(entities = [Quote::class], version = 1, exportSchema = false)
abstract class LocalQuoteDataBase : RoomDatabase() {

    abstract fun quotesDao(): QuotesDao
}
