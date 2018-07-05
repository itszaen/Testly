package com.zaen.testly.cas.views

import android.content.Context
import android.view.LayoutInflater

abstract class CardView(val context: Context){
    protected val inflater: LayoutInflater = LayoutInflater.from(context)

    abstract fun setUpPreview()
}