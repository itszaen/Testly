package com.zaen.testly.views.cards

import android.view.View

interface IMultipleSelectionCardView {
    interface MultipleOptionsClickListener{
        fun onAllCorrect()
        fun onCorrectOptionClick(view: View, index: Int)
        fun onIncorrectOptionClick(view: View, index: Int)
    }
}