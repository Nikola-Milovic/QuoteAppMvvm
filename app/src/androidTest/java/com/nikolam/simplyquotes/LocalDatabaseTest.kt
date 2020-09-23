package com.nikolam.simplyquotes

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.nikolam.simplyquotes.data.local.LocalQuoteDataBase
import com.nikolam.simplyquotes.data.local.QuotesDao
import org.junit.After
import org.junit.Before
import java.io.IOException


class SimpleEntityReadWriteTest {
    private lateinit var userDao: QuotesDao
    private lateinit var db: LocalQuoteDataBase

    @Before
    fun createDb() {
        @Before
        @Throws(Exception::class)
        fun setUp() {
            val component = DaggerTestApplicationComponent.factory().create(getApp())
            component.inject(getApp())

        }

        fun getApp() =
            InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestQuoteApplication


        val context = getApp().applicationContext
        db = Room.inMemoryDatabaseBuilder(
            context, LocalQuoteDataBase::class.java
        ).build()
        userDao = db.quotesDao()
    }

    private fun getApp() =
        InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestQuoteApplication


    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

//    @Test
//    @Throws(Exception::class)
//    fun writeUserAndReadInList() {
//        val user: User = TestUtil.createUser(3).apply {
//            setName("george")
//        }
//        userDao.insert(user)
//        val byName = userDao.findUsersByName("george")
//        assertThat(byName.get(0), equalTo(user))
//    }
}