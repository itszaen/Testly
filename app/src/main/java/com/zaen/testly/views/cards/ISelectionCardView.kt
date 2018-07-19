package com.zaen.testly.views.cards

import android.view.View
import java.util.*

interface ISelectionCardView {
    fun inflate(options: ArrayList<String>)
}
interface OptionClickListener{
    fun onCorrectOptionClick(view: View, index: Int)
    fun onIncorrectOptionClick(view: View, index: Int)
}