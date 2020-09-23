package com.nikolam.simplyquotes

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.nikolam.simplyquotes.data.model.Quote
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException


@ExperimentalCoroutinesApi
class QuoteTest {
    @get:Rule
    val rule = ActivityTestRule(MainActivity::class.java, true, false)

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val component = DaggerTestApplicationComponent.factory().create(getApp())
        component.inject(getApp())

    }

    private fun getApp() =
        InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestQuoteApplication

    @Test
    fun willDisplayQuote() {
        TestApplicationModule.mockQuoteDataSource.mockSuccess(
            listOf(
                Quote(
                    "Expected Author",
                    "Expected Quote",
                    false,
                    12312415
                )
            )
        )

        rule.launchActivity(null)

        onView(withId(R.id.textView_quote_text)).check(matches(withText("Expected Quote")))
        onView(withId(R.id.textView_quote_author)).check(matches(withText("Expected Author")))
    }


    @Test
    fun willDisplayErrorWhenQuoteCannotBeReturned() {
        TestApplicationModule.mockQuoteDataSource.mockFailure(IOException("Fake network error"))

        rule.launchActivity(null)

        onView(withId(R.id.textView_no_connection)).check(matches(isDisplayed()))
    }
}
