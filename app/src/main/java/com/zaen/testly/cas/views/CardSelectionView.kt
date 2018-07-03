package com.zaen.testly.cas.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.zaen.testly.R
import com.zaen.testly.data.SelectionCardData

class CardSelectionView(context: Context, val card: SelectionCardData, private val container: FrameLayout) : CardView(context) {
    private val cardSelectionView= inflater.inflate(R.layout.view_card_selection,container,false) as android.support.v7.widget.CardView
    private val questionTextView: android.support.v7.widget.AppCompatTextView
    private val optionContainer: LinearLayout
    init {
        container.addView(cardSelectionView)
        questionTextView = cardSelectionView.findViewById(R.id.text_card_selection_question)
        optionContainer = cardSelectionView.findViewById(R.id.option_container_card_selection)
    }
    fun inflate(){
        // Question
        questionTextView.text = card.question

        // Options
        for ((index, option) in card.options.withIndex()) {
            val optionItemLayout = inflater.inflate(R.layout.item_layout_linear_card_selection_option, optionContainer,false)
                    as android.support.v7.widget.CardView
            optionItemLayout.findViewById<android.support.v7.widget.AppCompatTextView>(R.id.text_card_selection_option).text = option
            optionContainer.addView(optionItemLayout)
        }
    }

    fun showAnswer(){
        optionContainer.getChildAt(card.answer).setBackgroundColor(ContextCompat.getColor(context, R.color.md_light_blue_100))
    }
}