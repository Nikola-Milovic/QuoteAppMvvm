package com.nikolam.simplyquotes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nikolam.simplyquotes.data.model.Quote

@Database(
    entities = [Quote::class],
    version = 1,
    exportSchema = false
) // data base for the quetes
abstract class LocalQuoteDataBase : RoomDatabase() {

    abstract fun quotesDao(): QuotesDao
}
