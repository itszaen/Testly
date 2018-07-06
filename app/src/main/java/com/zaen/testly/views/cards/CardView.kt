package com.zaen.testly.views.cards

import android.content.Context
import android.view.LayoutInflater

abstract class CardView(val context: Context) : IBaseCardView{
    protected val inflater: LayoutInflater = LayoutInflater.from(context)

    abstract fun setUpPreview()
}