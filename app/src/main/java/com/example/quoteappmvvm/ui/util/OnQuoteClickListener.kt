package com.example.quoteappmvvm.ui.util

import android.view.MotionEvent

interface OnQuoteClickListener {

    fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean

}