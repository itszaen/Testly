package com.zaen.testly.cas.views

import android.content.Context
import android.view.LayoutInflater

open class CardView(val context: Context){
    protected val inflater: LayoutInflater = LayoutInflater.from(context)
}