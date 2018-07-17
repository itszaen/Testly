package com.zaen.testly.views.cards

import android.view.View
import java.util.*

interface ISelectionCardView {
    fun inflate(options: ArrayList<String>)
}
interface OptionClickListener{
    fun onRightOptionClick(view: View, index: Int)
    fun onWrongOptionClick(view: View, index: Int)
}