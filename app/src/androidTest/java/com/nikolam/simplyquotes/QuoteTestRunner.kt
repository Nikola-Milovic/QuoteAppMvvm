package com.nikolam.simplyquotes

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class QuoteTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, TestQuoteApplication::class.java.name, context)
    }
}