package com.zaen.testly.cas.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View

open class CardView(context: Context) : View(context) {
    protected val inflater: LayoutInflater = LayoutInflater.from(context)
}