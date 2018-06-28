package com.zaen.testly.cas.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.zaen.testly.R
import com.zaen.testly.data.SelectionCardData

class CardSelectionView(val context: Context, val card: SelectionCardData, private val questionTextView: android.support.v7.widget.AppCompatTextView, private val optionContainer: LinearLayout) {
    private var optionItemLayout: android.support.constraint.ConstraintLayout? = null
    init {
        // Question
        questionTextView.text = card.question

        // Options
        for ((i, v) in card.options.withIndex()) {
            optionItemLayout = LayoutInflater.from(context).inflate(R.layout.item_layout_linear_card_selection_option, null) as android.support.constraint.ConstraintLayout
            (optionItemLayout?.getChildAt(0) as android.support.v7.widget.AppCompatTextView).text = v
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1F)
            optionItemLayout?.layoutParams = params
            optionContainer.addView(optionItemLayout)
        }
    }

    fun showAnswer(){
        optionContainer.getChildAt(card.answer).setBackgroundColor(ContextCompat.getColor(context, R.color.md_light_blue_100))
    }
}